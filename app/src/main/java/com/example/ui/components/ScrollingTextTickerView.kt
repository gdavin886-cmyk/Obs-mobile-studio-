package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MarqueeSpeed
import com.example.model.ScrollingTextConfig
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.blur
import com.example.model.TickerBackgroundStyle
import com.example.ui.theme.StudioObsidian

@Composable
fun ScrollingTextTickerView(
    config: ScrollingTextConfig,
    isLive: Boolean,
    onToggleEnabled: () -> Unit,
    onOpenEditDialog: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Show if enabled and (either permanent running is on or stream is live)
    val shouldShow = config.isEnabled && (config.isPermanentRunning || isLive)
    if (!shouldShow) return

    val density = LocalDensity.current
    var containerWidthPx by remember { mutableFloatStateOf(1000f) }
    var textWidthPx by remember { mutableFloatStateOf(1200f) }

    val speedDurationMs = when (config.speed) {
        MarqueeSpeed.SLOW -> 16000
        MarqueeSpeed.NORMAL -> 9500
        MarqueeSpeed.FAST -> 5000
    }

    val infiniteTransition = rememberInfiniteTransition(label = "marquee_anim")
    val totalDistance = (containerWidthPx + textWidthPx).coerceAtLeast(100f)

    val translationX by infiniteTransition.animateFloat(
        initialValue = containerWidthPx,
        targetValue = -textWidthPx,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = speedDurationMs,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "marquee_offset"
    )

    val badgeColor = remember(config.mod) {
        try {
            Color(android.graphics.Color.parseColor(config.mod.badgeColorHex))
        } catch (_: Exception) {
            Color(0xFFEF4444)
        }
    }

    val bannerBg = remember(config.backgroundColorHex, config.backgroundStyle) {
        when (config.backgroundStyle) {
            TickerBackgroundStyle.TRANSPARENT_GLASS -> Color(0x66000000)
            TickerBackgroundStyle.LIQUID_GLASS -> Color(0x33FFFFFF)
            TickerBackgroundStyle.BLOOD_GLASS -> Color(0x998B0000)
            TickerBackgroundStyle.SOLID_COLOR -> {
                try {
                    Color(android.graphics.Color.parseColor(config.backgroundColorHex))
                } catch (_: Exception) {
                    Color(0xE60A0E1A)
                }
            }
            TickerBackgroundStyle.CUSTOM_IMAGE -> Color.Transparent
        }
    }

    val bannerTextColor = remember(config.textColorHex) {
        try {
            Color(android.graphics.Color.parseColor(config.textColorHex))
        } catch (_: Exception) {
            Color.White
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp))
            .background(bannerBg)
            .border(0.5.dp, badgeColor.copy(alpha = 0.5f))
            .clickable { onOpenEditDialog() }
            .testTag("scrolling_ticker_view")
    ) {
        if (config.backgroundStyle == TickerBackgroundStyle.LIQUID_GLASS) {
            Box(modifier = Modifier.matchParentSize().blur(16.dp).background(Color(0x33FFFFFF)))
        }
        
        if (config.backgroundStyle == TickerBackgroundStyle.CUSTOM_IMAGE && !config.customBackgroundImageUri.isNullOrEmpty()) {
            AsyncImage(
                model = config.customBackgroundImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        }
        
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
        ) {
            // Prefix Mod Tag Badge
        if (config.showPrefixBadge) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(badgeColor)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
                    .padding(start = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = config.mod.prefixTag,
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        // Marquee Text Canvas
        Box(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 20.dp)
                .clipToBounds()
                .onSizeChanged { containerWidthPx = it.width.toFloat() }
        ) {
            Text(
                text = config.textContent,
                color = bannerTextColor,
                fontSize = config.fontSizeSp.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                softWrap = false,
                onTextLayout = { textLayoutResult ->
                    textWidthPx = textLayoutResult.size.width.toFloat()
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .graphicsLayer {
                        this.translationX = translationX
                    }
            )
        }

        // Quick On/Off toggle icon
        Box(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(18.dp)
                .clip(CircleShape)
                .background(Color(0x66000000))
                .clickable { onToggleEnabled() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Turn off ticker",
                tint = Color(0xFFCBD5E1),
                modifier = Modifier.size(12.dp)
            )
        }
        }
    }
}
