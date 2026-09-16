package com.example.ui.components

import android.net.Uri
import android.widget.VideoView
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.CustomMediaCastConfig
import com.example.model.CustomMediaType
import com.example.ui.theme.*
import com.example.util.FileUtils

@Composable
fun MediaCastSourceView(
    modifier: Modifier = Modifier,
    mediaConfig: CustomMediaCastConfig = CustomMediaCastConfig(),
    isPlaying: Boolean = true,
    onTogglePlay: () -> Unit = {},
    onUploadClick: () -> Unit = {},
    onClearClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val infiniteTransition = rememberInfiniteTransition(label = "media_anim")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "mediaProgress"
    )

    val formatBadge = FileUtils.getFormatBadge(mediaConfig.fileName, mediaConfig.mimeType)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF07090E)),
        contentAlignment = Alignment.Center
    ) {
        // Active Media Content Canvas
        if (!mediaConfig.uri.isNullOrEmpty()) {
            if (mediaConfig.mediaType == CustomMediaType.VIDEO) {
                // Native Android VideoView for hardware-accelerated video decoding
                AndroidView(
                    factory = { ctx ->
                        VideoView(ctx).apply {
                            try {
                                setVideoURI(Uri.parse(mediaConfig.uri))
                                setOnPreparedListener { mp ->
                                    mp.isLooping = mediaConfig.isLooping
                                    if (isPlaying) start()
                                }
                                setOnErrorListener { _, _, _ ->
                                    true // Handled gracefully
                                }
                            } catch (_: Exception) {}
                        }
                    },
                    update = { videoView ->
                        try {
                            if (isPlaying) {
                                if (!videoView.isPlaying) videoView.start()
                            } else {
                                if (videoView.isPlaying) videoView.pause()
                            }
                        } catch (_: Exception) {}
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Static or animated GIF / SVG / PNG / JPG image display via Coil
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(mediaConfig.uri)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Cast Media Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            // Fallback virtual studio background presentation reel
            Image(
                painter = painterResource(id = R.drawable.studio_virtual_bg_1789472361323),
                contentDescription = "Media Video Source",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Vignette gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0x66000000), Color(0x33000000), Color(0xBB000000))
                        )
                    )
            )

            // Center Media watermark & waveform visualizer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color(0x88000000))
                        .border(1.5.dp, StudioCyan, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = onTogglePlay) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Media Playback",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = mediaConfig.fileName,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Hardware Accelerated Decode (NVDEC / MediaCodec)",
                    color = StudioCyan,
                    fontSize = 9.sp
                )
            }
        }

        // Top Overlay Bar: Format Badge + Direct Upload Media Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Status Chip
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xCC0D1017))
                    .border(0.8.dp, StudioCyan, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (isPlaying) StudioNeonGreen else StudioRecRed)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = formatBadge,
                    color = StudioCyan,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Quick Upload Media Button on Video Screen
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onUploadClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xDD00E5FF)),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(30.dp)
                        .testTag("upload_media_stream_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = StudioObsidian,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Upload Media",
                        color = StudioObsidian,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                if (!mediaConfig.uri.isNullOrEmpty()) {
                    IconButton(
                        onClick = onClearClick,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xCCFF1744))
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reset",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Lower-Third Broadcast Graphic
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 10.dp, vertical = 24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xDD0D1017))
                .border(1.dp, StudioPurple, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(StudioNeonGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CURRENT CASTING: ${if (mediaConfig.mediaType == CustomMediaType.VIDEO) "VIDEO" else "IMAGE"}",
                        color = StudioCyan,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
                Text(
                    text = mediaConfig.fileName,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Bottom Media Playback Scrubber Bar & Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color(0xCC0A0D14))
                .padding(bottom = 2.dp)
        ) {
            LinearProgressIndicator(
                progress = { if (isPlaying) progress else 0.45f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = StudioCyan,
                trackColor = Color(0x44FFFFFF)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onTogglePlay,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPlaying) "PLAYING" else "PAUSED",
                        color = if (isPlaying) StudioNeonGreen else TextMuted,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Text(
                    text = "00:24 / 03:10 [HW ACCEL]",
                    color = TextMuted,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
