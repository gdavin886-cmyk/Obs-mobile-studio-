package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.*
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioNeonGreen

@Composable
fun CustomLogoWatermarkView(
    config: CustomLogoConfig,
    modifier: Modifier = Modifier,
    onCountdownFinished: () -> Unit = {}
) {
    if (!config.isEnabled) return

    val alignment = when (config.position) {
        LogoPosition.TOP_LEFT -> Alignment.TopStart
        LogoPosition.TOP_RIGHT -> Alignment.TopEnd
        LogoPosition.BOTTOM_LEFT -> Alignment.BottomStart
        LogoPosition.BOTTOM_RIGHT -> Alignment.BottomEnd
    }

    val logoSizeDp = (config.sizePercent * 1.8f).dp.coerceIn(28.dp, 80.dp)

    // Parse background color based on mode and on/off switch
    val backgroundColor = if (!config.showBackground) {
        Color.Transparent
    } else {
        when (config.backgroundColorMode) {
            WatermarkBgColorMode.TRANSPARENT -> Color.Transparent
            WatermarkBgColorMode.DARK_GLASS -> Color(0x990A0D14)
            WatermarkBgColorMode.SOLID_BLACK -> Color(0xFF090D16)
            WatermarkBgColorMode.STUDIO_CYAN -> Color(0x99002B36)
            WatermarkBgColorMode.CYBER_PURPLE -> Color(0x992E1065)
            WatermarkBgColorMode.CUSTOM -> {
                try {
                    Color(android.graphics.Color.parseColor(config.customBgColorHex)).copy(alpha = 0.85f)
                } catch (_: Exception) {
                    Color(0x990A0D14)
                }
            }
        }
    }

    val hasBackground = config.showBackground && config.backgroundColorMode != WatermarkBgColorMode.TRANSPARENT

    // Countdown formatting
    val countdownFormatted = remember(config.countdownRemainingSeconds) {
        val mins = config.countdownRemainingSeconds / 60
        val secs = config.countdownRemainingSeconds % 60
        String.format("%02d:%02d", mins, secs)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        // Main container with optional background and removed blue frame
        Box(
            modifier = Modifier
                .align(alignment)
                .alpha(config.opacity)
                .then(
                    if (hasBackground) {
                        Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(backgroundColor)
                            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 7.dp, vertical = 5.dp)
                    } else {
                        Modifier.padding(4.dp)
                    }
                )
        ) {
            // Layout arrangement depending on countdown position
            val contentWithCountdown = @Composable {
                // Arrangement of Logo + Watermark text relative to logo {under, left, top or right}
                val logoWithTextContent = @Composable {
                    val logoComposable = @Composable {
                        Box(
                            modifier = Modifier
                                .size(logoSizeDp)
                                .clip(RoundedCornerShape(6.dp))
                                .then(
                                    // Blue frame removed! Only show subtle frame if explicitly enabled
                                    if (config.showLogoFrame) {
                                        Modifier.border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                    } else {
                                        Modifier
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!config.customImageUri.isNullOrEmpty()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(config.customImageUri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Custom Uploaded Logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.obs_studio_logo_1789472344798),
                                    contentDescription = "Default OBS Studio Logo",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            // If countdown is inside badge
                            if (config.isCountdownEnabled && config.countdownPosition == CountdownPosition.INSIDE) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .background(Color(0xCC000000))
                                        .padding(vertical = 1.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = countdownFormatted,
                                        color = StudioAmber,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }
                    }

                    val textComposable = @Composable {
                        if (config.showTextLabel && config.watermarkText.isNotEmpty()) {
                            Text(
                                text = config.watermarkText,
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.8.sp
                            )
                        }
                    }

                    // Positioning watermark name relative to logo {UNDER, LEFT, TOP, RIGHT}
                    when (config.textPosition) {
                        TextRelativePosition.RIGHT -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                logoComposable()
                                textComposable()
                            }
                        }
                        TextRelativePosition.LEFT -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                textComposable()
                                logoComposable()
                            }
                        }
                        TextRelativePosition.TOP -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                textComposable()
                                logoComposable()
                            }
                        }
                        TextRelativePosition.UNDER -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                logoComposable()
                                textComposable()
                            }
                        }
                    }
                }

                // If Countdown is enabled, place it according to {under, left, top, right}
                if (config.isCountdownEnabled && config.countdownPosition != CountdownPosition.INSIDE) {
                    val countdownBadge = @Composable {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xDD0F172A))
                                .border(0.5.dp, StudioAmber, RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = StudioAmber,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = countdownFormatted,
                                color = StudioAmber,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    when (config.countdownPosition) {
                        CountdownPosition.UNDER -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                logoWithTextContent()
                                countdownBadge()
                            }
                        }
                        CountdownPosition.TOP -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                countdownBadge()
                                logoWithTextContent()
                            }
                        }
                        CountdownPosition.LEFT -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                countdownBadge()
                                logoWithTextContent()
                            }
                        }
                        CountdownPosition.RIGHT -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                logoWithTextContent()
                                countdownBadge()
                            }
                        }
                        CountdownPosition.INSIDE -> {
                            logoWithTextContent()
                        }
                    }
                } else {
                    logoWithTextContent()
                }
            }

            contentWithCountdown()
        }
    }
}
