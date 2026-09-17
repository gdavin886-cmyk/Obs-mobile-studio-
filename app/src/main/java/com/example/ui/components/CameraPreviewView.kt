package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.SurfaceHolder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioObsidian
import com.example.stream.StreamManager
import com.pedro.library.view.OpenGlView
import com.pedro.encoder.input.video.CameraHelper

@Composable
fun CameraPreviewView(
    modifier: Modifier = Modifier,
    isFrontCamera: Boolean = false,
    isTorchOn: Boolean = false,
    isChromaKeyEnabled: Boolean = false,
    keyColorHex: String = "#00FF00"
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    
    // Handle camera switching
    LaunchedEffect(isFrontCamera) {
        if (StreamManager.isReady && StreamManager.rtmpCamera?.isOnPreview == true) {
            val isCurrentFront = StreamManager.rtmpCamera?.isFrontCamera ?: false
            if (isCurrentFront != isFrontCamera) {
                try {
                    StreamManager.rtmpCamera?.switchCamera()
                } catch (e: Exception) {
                    Log.e("CameraPreviewView", "Error switching camera", e)
                }
            }
        }
    }
    
    // Handle torch
    LaunchedEffect(isTorchOn) {
        if (StreamManager.isReady && StreamManager.rtmpCamera?.isStreaming == true) {
            try {
                if (isTorchOn) StreamManager.rtmpCamera?.enableLantern() else StreamManager.rtmpCamera?.disableLantern()
            } catch (e: Exception) {
                Log.e("CameraPreviewView", "Error toggling torch", e)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val openGlView = OpenGlView(ctx)
                    StreamManager.init(openGlView, ctx)
                    openGlView.holder.addCallback(object : SurfaceHolder.Callback {
                        override fun surfaceCreated(holder: SurfaceHolder) {
                            // Surface is created
                        }

                        override fun surfaceChanged(
                            holder: SurfaceHolder,
                            format: Int,
                            width: Int,
                            height: Int
                        ) {
                            if (holder.surface.isValid) {
                                if (StreamManager.rtmpCamera != null && StreamManager.openGlView !== openGlView) {
                                    StreamManager.init(openGlView, ctx)
                                }
                                val facing = if (isFrontCamera) CameraHelper.Facing.FRONT else CameraHelper.Facing.BACK
                                StreamManager.startPreview(facing)
                            }
                        }

                        override fun surfaceDestroyed(holder: SurfaceHolder) {
                            StreamManager.handleSurfaceDestroyed(ctx)
                        }
                    })
                    openGlView
                },
                onRelease = {
                    StreamManager.handleSurfaceDestroyed(context)
                },
                modifier = Modifier.fillMaxSize()
            )

            // Chroma Key green screen simulation tint if chroma key is previewed
            if (isChromaKeyEnabled) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color(0x2200E676)
                                )
                            )
                        )
                )
            }
        } else {
            // Placeholder when permission is not granted
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(StudioObsidian)
                        .border(1.dp, StudioCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Videocam,
                        contentDescription = "Camera Permission Required",
                        tint = StudioCyan,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Studio Camera Source",
                    color = Color.White,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Tap to grant camera access for live broadcast feed",
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("enable_camera_btn")
                ) {
                    Text("Grant Camera Access", color = StudioObsidian, fontSize = 12.sp)
                }
            }
        }
    }
}
