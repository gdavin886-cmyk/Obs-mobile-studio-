package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ScreenCastSimulatorView(
    modifier: Modifier = Modifier,
    isPipFacecamVisible: Boolean = true,
    isFrontCamera: Boolean = false,
    isTorchOn: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "screen_anim")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    val gridOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gridOffset"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0F172A), Color(0xFF020617))
                )
            )
    ) {
        // High-tech simulated mobile game canvas background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Animated grid lines representing mobile 3D game engine
            val gridStep = 40f
            var y = gridOffset
            while (y < canvasHeight) {
                drawLine(
                    color = Color(0x2200E5FF),
                    start = Offset(0f, y),
                    end = Offset(canvasWidth, y),
                    strokeWidth = 1f
                )
                y += gridStep
            }

            var x = 0f
            while (x < canvasWidth) {
                drawLine(
                    color = Color(0x1500E5FF),
                    start = Offset(x, 0f),
                    end = Offset(x, canvasHeight),
                    strokeWidth = 1f
                )
                x += gridStep
            }

            // Radar circle in center
            drawCircle(
                color = Color(0x207C4DFF),
                radius = canvasHeight * 0.35f,
                center = Offset(canvasWidth * 0.5f, canvasHeight * 0.5f),
                style = Stroke(width = 2f)
            )
            drawCircle(
                color = Color(0x3000E5FF),
                radius = canvasHeight * 0.20f,
                center = Offset(canvasWidth * 0.5f, canvasHeight * 0.5f),
                style = Stroke(width = 1.5f)
            )
        }

        // Screen Cast Overlay Info (Game HUD)
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = null,
                    tint = StudioCyan,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "MOBILE SCREEN CAST • 1080x2400 @ 60 FPS",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "CYBER RUSH 2077 [LIVE GAMEPLAY]",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Score & Game Stats (Top Right of game feed)
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        ) {
            Text(
                text = "SCORE: 184,920",
                color = StudioAmber,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "RANK #1 • 14 KILLS",
                color = StudioNeonGreen,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Picture-in-Picture (PIP) Facecam overlay in Bottom-Left corner
        if (isPipFacecamVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .width(110.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
                    .border(2.dp, StudioCyan, RoundedCornerShape(8.dp))
            ) {
                CameraPreviewView(
                    modifier = Modifier.fillMaxSize(),
                    isFrontCamera = true,
                    isTorchOn = isTorchOn
                )

                // Facecam watermark badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .background(Color(0xCC000000))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(StudioLiveRed)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "FACECAM",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
