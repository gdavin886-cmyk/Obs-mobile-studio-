package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.CustomLogoConfig
import com.example.model.LogoPosition
import com.example.ui.theme.StudioCyan

@Composable
fun CustomLogoWatermarkView(
    config: CustomLogoConfig,
    modifier: Modifier = Modifier
) {
    if (!config.isEnabled) return

    val alignment = when (config.position) {
        LogoPosition.TOP_LEFT -> Alignment.TopStart
        LogoPosition.TOP_RIGHT -> Alignment.TopEnd
        LogoPosition.BOTTOM_LEFT -> Alignment.BottomStart
        LogoPosition.BOTTOM_RIGHT -> Alignment.BottomEnd
    }

    val logoSizeDp = (config.sizePercent * 1.8f).dp.coerceIn(28.dp, 80.dp)

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(alignment)
                .alpha(config.opacity)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0x990A0D14))
                .border(0.8.dp, Color(0x44FFFFFF), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(logoSizeDp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(1.dp, StudioCyan, RoundedCornerShape(6.dp)),
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
            }

            if (config.showTextLabel && config.watermarkText.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = config.watermarkText,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
