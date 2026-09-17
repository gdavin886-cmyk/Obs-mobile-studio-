package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioLiveRed
import com.example.ui.theme.StudioObsidian

@Composable
fun CameraPreviewView(
    modifier: Modifier = Modifier,
    isFrontCamera: Boolean = false,
    isTorchOn: Boolean = false,
    isChromaKeyEnabled: Boolean = false,
    keyColorHex: String = "#00FF00"
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                    }
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val cameraSelector = if (isFrontCamera) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                            try {
                                cameraProvider.unbindAll()
                                val camera = cameraProvider.bindToLifecycle(
                                    lifecycleOwner,
                                    cameraSelector,
                                    preview
                                )
                                if (camera.cameraInfo.hasFlashUnit()) {
                                    camera.cameraControl.enableTorch(isTorchOn)
                                }
                            } catch (exc: Exception) {
                                // Camera binding failed (e.g. emulator missing camera hardware)
                            }
                        } catch (e: Exception) {
                            // CameraProvider future failed or timed out
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
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
            // High-fidelity Studio Camera Standby placeholder when permission is not granted
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
