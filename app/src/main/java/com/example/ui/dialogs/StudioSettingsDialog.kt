package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun StudioSettingsDialog(
    settings: StudioSettings,
    encoderConfig: EncoderConfig,
    destinations: List<StreamDestination>,
    isLive: Boolean,
    onUpdateSettings: (StudioSettings) -> Unit,
    onUpdateEncoder: (EncoderConfig) -> Unit,
    onToggleDestination: (String) -> Unit,
    onOpenDestinationsFull: () -> Unit,
    onOpenWebCastFullScreen: () -> Unit,
    onRestartStudioResetMemory: () -> Unit,
    onOpenLogoSettings: () -> Unit = {},
    onOpenTickerSettings: () -> Unit = {},
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableIntStateOf(0) } // 0: Live & BG, 1: Platforms, 2: Video/Audio, 3: Memory & Reset
    var showRestartConfirmDialog by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
            border = BorderStroke(1.dp, StudioCyan),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(4.dp)
                .testTag("studio_settings_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Settings Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(StudioCardBg)
                            .border(1.dp, StudioCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Studio Settings",
                            tint = StudioCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "STUDIO SETTINGS & CONTROL",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Standard Broadcast, Background & Persistence",
                            color = StudioCyan,
                            fontSize = 9.sp
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Standard Categories Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedCategory,
                    containerColor = StudioObsidian,
                    contentColor = StudioCyan,
                    edgePadding = 0.dp
                ) {
                    Tab(
                        selected = selectedCategory == 0,
                        onClick = { selectedCategory = 0 },
                        text = {
                            Text(
                                text = "LIVE & BG",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    )
                    Tab(
                        selected = selectedCategory == 1,
                        onClick = { selectedCategory = 1 },
                        text = {
                            Text(
                                text = "OK.RU & TG",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    )
                    Tab(
                        selected = selectedCategory == 2,
                        onClick = { selectedCategory = 2 },
                        text = {
                            Text(
                                text = "VIDEO / WEB",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    )
                    Tab(
                        selected = selectedCategory == 3,
                        onClick = { selectedCategory = 3 },
                        text = {
                            Text(
                                text = "MEMORY & RESET",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when (selectedCategory) {
                        0 -> {
                            // Section: Live & Background Running
                            SettingsSectionHeader(
                                title = "BACKGROUND LIVE & NOTIFICATIONS",
                                icon = Icons.Default.Sensors
                            )

                            SettingsSwitchItem(
                                title = "Background Live Running",
                                subtitle = "Keep broadcast encoder and audio running when app is minimized or screen locked",
                                isChecked = settings.backgroundLiveEnabled,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(backgroundLiveEnabled = it))
                                },
                                testTag = "setting_switch_bg_live"
                            )

                            SettingsSwitchItem(
                                title = "Notification Pop-Up Live (Heads-Up Banner)",
                                subtitle = "Show interactive high-priority pop-up notification with Live On/Off & Mute buttons",
                                isChecked = settings.notificationPopupLive,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(notificationPopupLive = it))
                                },
                                testTag = "setting_switch_notification_popup"
                            )

                            SettingsSwitchItem(
                                title = "Keep Screen Awake (Wake Lock)",
                                subtitle = "Prevent device screen from sleeping while in studio or live broadcast",
                                isChecked = settings.keepScreenAwake,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(keepScreenAwake = it))
                                },
                                testTag = "setting_switch_keep_awake"
                            )

                            SettingsSwitchItem(
                                title = "Auto Reconnect Ingest",
                                subtitle = "Automatically retry RTMP/RTMPS stream handshake if mobile network jitters",
                                isChecked = settings.autoReconnect,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(autoReconnect = it))
                                },
                                testTag = "setting_switch_auto_reconnect"
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Watermark & Ticker Overlay Config Short-links
                            SettingsSectionHeader(
                                title = "WATERMARK & SCROLLING TICKER OVERLAYS",
                                icon = Icons.Default.BrandingWatermark
                            )

                            Button(
                                onClick = onOpenLogoSettings,
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCardBg),
                                border = BorderStroke(1.dp, StudioCyan),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("settings_open_logo_config")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BrandingWatermark,
                                    contentDescription = null,
                                    tint = StudioCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Watermark Logo & Countdown",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Frame removal, background on/off, text align & auto-next name",
                                        color = TextMuted,
                                        fontSize = 8.sp
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = StudioCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Button(
                                onClick = onOpenTickerSettings,
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCardBg),
                                border = BorderStroke(1.dp, StudioAmber),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("settings_open_ticker_config")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = StudioAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Scrolling News & Inform Ticker",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Marquee mods, permanent running toggle & broadcast speed",
                                        color = TextMuted,
                                        fontSize = 8.sp
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = StudioAmber,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        1 -> {
                            // Section: OK.ru, Telegram & Multi-Broadcasting
                            SettingsSectionHeader(
                                title = "BROADCAST DESTINATIONS (OK.RU, TELEGRAM, ETC.)",
                                icon = Icons.Default.CloudUpload
                            )

                            Text(
                                text = "Toggle platforms to broadcast simultaneously across all active streams:",
                                color = TextMuted,
                                fontSize = 10.sp
                            )

                            destinations.forEach { dest ->
                                val badgeColor = when (dest.platform) {
                                    DestinationPlatform.OK_RU -> Color(0xFFEE8208)
                                    DestinationPlatform.TELEGRAM -> Color(0xFF24A1DE)
                                    DestinationPlatform.TWITCH -> TwitchPurple
                                    DestinationPlatform.YOUTUBE -> YouTubeRed
                                    DestinationPlatform.FACEBOOK -> FacebookBlue
                                    DestinationPlatform.CUSTOM_RTMPS -> RtmpsGold
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(StudioCardBg)
                                        .border(1.dp, if (dest.isEnabled) badgeColor else StudioCardBorder, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 10.dp, vertical = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(badgeColor)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = dest.platform.platformName,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (dest.isEnabled) "ACTIVE INGEST • ${dest.serverUrl}" else "DISABLED",
                                            color = if (dest.isEnabled) StudioNeonGreen else TextMuted,
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            maxLines = 1
                                        )
                                    }
                                    Switch(
                                        checked = dest.isEnabled,
                                        onCheckedChange = { onToggleDestination(dest.id) },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = badgeColor,
                                            checkedTrackColor = badgeColor.copy(alpha = 0.3f)
                                        )
                                    )
                                }
                            }

                            Button(
                                onClick = onOpenDestinationsFull,
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = StudioObsidian,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Edit Stream Keys & Ingest URLs",
                                    color = StudioObsidian,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        2 -> {
                            // Section: Video Encoder & WebCast Controls
                            SettingsSectionHeader(
                                title = "WEBCAST & TOUCH INTERACTION",
                                icon = Icons.Default.Language
                            )

                            SettingsSwitchItem(
                                title = "WebCast Touch & Scrolling Interaction",
                                subtitle = "Allow interactive touch scrolling and clicking links inside casted website",
                                isChecked = settings.webTouchInteractive,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(webTouchInteractive = it))
                                },
                                testTag = "setting_switch_web_touch"
                            )

                            OutlinedButton(
                                onClick = onOpenWebCastFullScreen,
                                border = BorderStroke(1.dp, StudioCyan),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = null,
                                    tint = StudioCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Launch WebCast in Full Screen Interface",
                                    color = StudioCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            SettingsSectionHeader(
                                title = "VIDEO HARDWARE ENCODER",
                                icon = Icons.Default.Videocam
                            )

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(StudioCardBg)
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Active Codec",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = encoderConfig.codec.codecName,
                                        color = StudioCyan,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(StudioPurple)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (encoderConfig.codec.isHardware) "HARDWARE ACCELERATED" else "SOFTWARE",
                                        color = Color.White,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(StudioCardBg)
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Resolution & Frame Rate",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${encoderConfig.resolution.label} • ${encoderConfig.fps} FPS",
                                        color = StudioNeonGreen,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                                Text(
                                    text = "${encoderConfig.targetBitrateKbps} kbps",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        3 -> {
                            // Section: Memory Persistence & Restart Studio
                            SettingsSectionHeader(
                                title = "APP OPEN MEMORY & STATE RETENTION",
                                icon = Icons.Default.Save
                            )

                            SettingsSwitchItem(
                                title = "Retain Studio Memory On Reopen",
                                subtitle = "Never lose scenes, URLs, stream keys, audio levels, or active layers when reopening the app",
                                isChecked = settings.retainMemoryOnOpen,
                                onCheckedChange = {
                                    onUpdateSettings(settings.copy(retainMemoryOnOpen = it))
                                },
                                testTag = "setting_switch_retain_memory"
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            SettingsSectionHeader(
                                title = "RESTART STUDIO / RESET MEMORY",
                                icon = Icons.Default.RestartAlt
                            )

                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF241215)),
                                border = BorderStroke(1.dp, StudioRecRed.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = StudioRecRed,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Restart Studio & Clear Memory Cache",
                                            color = StudioRecRed,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Reset all scenes, destinations, custom URLs, and audio mixers to pristine factory defaults. Current live broadcast will safely stop.",
                                        color = Color(0xFFE2E8F0),
                                        fontSize = 9.sp,
                                        lineHeight = 13.sp
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = { showRestartConfirmDialog = true },
                                        colors = ButtonDefaults.buttonColors(containerColor = StudioRecRed),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(38.dp)
                                            .testTag("restart_studio_reset_memory_btn")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.RestartAlt,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "RESTART STUDIO & RESET MEMORY",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Close Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .testTag("close_settings_btn")
                ) {
                    Text(
                        text = "Close Settings",
                        color = StudioObsidian,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Confirmation Alert for Restart
    if (showRestartConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showRestartConfirmDialog = false },
            containerColor = StudioDarkSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = null,
                        tint = StudioRecRed
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Restart Studio?",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Text(
                    text = "Are you sure you want to restart the studio? All custom URLs, audio fader levels, and saved preferences will be re-initialized to initial defaults.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showRestartConfirmDialog = false
                        onRestartStudioResetMemory()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioRecRed)
                ) {
                    Text("Restart & Reset", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showRestartConfirmDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = StudioCyan,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            color = StudioCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudioCardBg)
            .border(1.dp, if (isChecked) StudioCyan.copy(alpha = 0.5f) else StudioCardBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 9.sp,
                lineHeight = 12.sp
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = StudioCyan,
                checkedTrackColor = StudioCyan.copy(alpha = 0.3f)
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}
