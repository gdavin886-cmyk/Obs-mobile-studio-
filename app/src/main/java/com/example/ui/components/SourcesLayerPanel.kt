package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SourceItem
import com.example.model.SourceType
import com.example.ui.theme.*

@Composable
fun SourcesLayerPanel(
    sources: List<SourceItem>,
    onToggleVisibility: (String) -> Unit,
    onToggleLock: (String) -> Unit,
    onOpenChromaKey: () -> Unit,
    onOpenLogoEditor: () -> Unit,
    onOpenTickerEditor: () -> Unit = {},
    onSwitchCamera: () -> Unit,
    onToggleTorch: () -> Unit,
    isTorchOn: Boolean,
    onOpenMediaPicker: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(StudioDarkSurface)
            .border(1.dp, StudioCardBorder)
            .padding(8.dp)
    ) {
        // Header: Title & Camera Hardware Controls
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "SOURCES & OVERLAYS",
                    color = StudioCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "CURRENT: ${sources.count { it.isVisible }} of ${sources.size} active layers",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Camera Flip
            IconButton(
                onClick = onSwitchCamera,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(StudioCardBg)
                    .border(1.dp, StudioCardBorder, RoundedCornerShape(6.dp))
                    .testTag("flip_camera_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Switch Camera Front/Back",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Torch Toggle
            IconButton(
                onClick = onToggleTorch,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isTorchOn) StudioAmber else StudioCardBg)
                    .border(1.dp, if (isTorchOn) StudioAmber else StudioCardBorder, RoundedCornerShape(6.dp))
                    .testTag("toggle_torch_btn")
            ) {
                Icon(
                    imageVector = if (isTorchOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Toggle Torch",
                    tint = if (isTorchOn) StudioObsidian else Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Quick Filter Action Row (Chroma Key, Custom Logo, Upload Media)
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Quick Filter: Chroma Key
            Button(
                onClick = onOpenChromaKey,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F2E1E)),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonGreen),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .testTag("open_chroma_filter_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.FilterVintage,
                    contentDescription = "Chroma Key",
                    tint = StudioNeonGreen,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "CHROMA",
                    color = StudioNeonGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Quick Filter: Custom Logo
            Button(
                onClick = onOpenLogoEditor,
                colors = ButtonDefaults.buttonColors(containerColor = StudioCardBg),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan),
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .testTag("open_logo_editor_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.BrandingWatermark,
                    contentDescription = "Custom Logo",
                    tint = StudioCyan,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "LOGO",
                    color = StudioCyan,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Quick Media Upload: Video / Image
            Button(
                onClick = onOpenMediaPicker,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1B4B)),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioPurple),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .testTag("open_media_upload_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = "Upload Media",
                    tint = StudioPurple,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "MEDIA",
                    color = StudioPurple,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Quick Ticker: Scrolling News / Inform
            Button(
                onClick = onOpenTickerEditor,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1917)),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioAmber),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .testTag("open_ticker_editor_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = "Scrolling Ticker",
                    tint = StudioAmber,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "TICKER",
                    color = StudioAmber,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Sources List
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            sources.forEach { source ->
                val sourceIcon: ImageVector = when (source.type) {
                    SourceType.CAMERA -> Icons.Default.Videocam
                    SourceType.SCREEN -> Icons.Default.ScreenShare
                    SourceType.WEB_PAGE -> Icons.Default.Language
                    SourceType.FILE_MEDIA -> Icons.Default.VideoLibrary
                    SourceType.CUSTOM_LOGO -> Icons.Default.BrandingWatermark
                    SourceType.ALERT_OVERLAY -> Icons.Default.NotificationsActive
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(StudioCardBg)
                        .border(
                            0.5.dp,
                            if (source.isVisible) StudioCardBorder else Color(0x22FFFFFF),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    // Visibility Toggle (Eye icon) with comfortable 32.dp touch target
                    IconButton(
                        onClick = { onToggleVisibility(source.id) },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("toggle_vis_${source.id}")
                    ) {
                        Icon(
                            imageVector = if (source.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle Visibility",
                            tint = if (source.isVisible) StudioCyan else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Source Type Icon
                    Icon(
                        imageVector = sourceIcon,
                        contentDescription = null,
                        tint = if (source.isVisible) Color.White else TextMuted,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Source Name & Current Status Tag
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = source.name,
                            color = if (source.isVisible) Color.White else TextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        Text(
                            text = if (source.isVisible) "STATUS: VISIBLE" else "STATUS: HIDDEN",
                            color = if (source.isVisible) StudioNeonGreen else TextMuted,
                            fontSize = 8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Lock Icon with comfortable 32.dp touch target
                    IconButton(
                        onClick = { onToggleLock(source.id) },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("toggle_lock_${source.id}")
                    ) {
                        Icon(
                            imageVector = if (source.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Lock Source Transform",
                            tint = if (source.isLocked) StudioAmber else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
