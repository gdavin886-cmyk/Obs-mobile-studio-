package com.example.ui.dialogs

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun StreamDestinationsDialog(
    destinations: List<StreamDestination>,
    encoderConfig: EncoderConfig,
    isLive: Boolean,
    onToggleDestination: (String) -> Unit,
    onUpdateDestination: (StreamDestination) -> Unit,
    onUpdateEncoder: (EncoderConfig) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Output Platforms, 1: Hardware Encoder
    var currentEncoder by remember { mutableStateOf(encoderConfig) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Dialog Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(StudioCardBg)
                            .border(1.dp, StudioCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = StudioCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BROADCAST DESTINATIONS & ENCODER",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Multi-Platform RTMPS Ingest & Hardware Acceleration",
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

                Spacer(modifier = Modifier.height(12.dp))

                // Navigation Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = StudioObsidian,
                    contentColor = StudioCyan
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                text = "DESTINATIONS (${destinations.count { it.isEnabled }})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                text = "HW ENCODER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (selectedTab == 0) {
                    // Destinations Tab
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        destinations.forEach { dest ->
                            DestinationCard(
                                destination = dest,
                                isLive = isLive,
                                onToggle = { onToggleDestination(dest.id) },
                                onUpdateKey = { newKey ->
                                    onUpdateDestination(dest.copy(streamKey = newKey))
                                },
                                onUpdateUrl = { newUrl ->
                                    onUpdateDestination(dest.copy(serverUrl = newUrl))
                                }
                            )
                        }
                    }
                } else {
                    // Hardware Encoder Tab
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Codec Selector
                        Text(
                            text = "VIDEO ENCODER CODEC (CURRENT: ${currentEncoder.codec.codecName})",
                            color = StudioCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )

                        HardwareCodec.values().forEach { codec ->
                            val isSelected = currentEncoder.codec == codec
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) StudioSurfaceVariant else StudioCardBg)
                                    .border(
                                        1.dp,
                                        if (isSelected) StudioCyan else StudioCardBorder,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        currentEncoder = currentEncoder.copy(codec = codec)
                                        onUpdateEncoder(currentEncoder)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        currentEncoder = currentEncoder.copy(codec = codec)
                                        onUpdateEncoder(currentEncoder)
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = StudioCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = codec.codecName,
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${codec.chipVendor} • ${if (isSelected) "CURRENTLY ACTIVE" else "AVAILABLE"}",
                                        color = if (codec.isHardware) StudioNeonGreen else TextMuted,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        // Resolution Selector
                        Text(
                            text = "STREAM RESOLUTION (CURRENT: ${currentEncoder.resolution.label})",
                            color = StudioCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )

                        StreamResolution.values().forEach { res ->
                            val isSelected = currentEncoder.resolution == res
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) StudioSurfaceVariant else StudioCardBg)
                                    .border(
                                        1.dp,
                                        if (isSelected) StudioPurple else StudioCardBorder,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .clickable {
                                        currentEncoder = currentEncoder.copy(
                                            resolution = res,
                                            targetBitrateKbps = res.defaultBitrate,
                                            fps = if (res.label.contains("60")) 60 else 30
                                        )
                                        onUpdateEncoder(currentEncoder)
                                    }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        currentEncoder = currentEncoder.copy(
                                            resolution = res,
                                            targetBitrateKbps = res.defaultBitrate,
                                            fps = if (res.label.contains("60")) 60 else 30
                                        )
                                        onUpdateEncoder(currentEncoder)
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = StudioPurple)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = res.label,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "${res.width}x${res.height} • Suggested: ${res.defaultBitrate} kbps",
                                        color = TextMuted,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                        }

                        // Bitrate Slider
                        Text(
                            text = "CURRENT TARGET BITRATE: ${currentEncoder.targetBitrateKbps} KBPS",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Slider(
                            value = currentEncoder.targetBitrateKbps.toFloat(),
                            onValueChange = {
                                currentEncoder = currentEncoder.copy(targetBitrateKbps = it.toInt())
                                onUpdateEncoder(currentEncoder)
                            },
                            valueRange = 1000f..12000f,
                            steps = 22,
                            colors = SliderDefaults.colors(
                                thumbColor = StudioCyan,
                                activeTrackColor = StudioCyan,
                                inactiveTrackColor = StudioCardBorder
                            )
                        )

                        // Low-Latency Streaming Toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(StudioCardBg)
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Low Latency Streaming (LL-HLS / RTMPS)",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (currentEncoder.isLowLatencyMode) "CURRENT: ULTRA-LOW DELAY (< 40ms)" else "CURRENT: STANDARD BUFFER DELAY (~ 2-3s)",
                                    color = if (currentEncoder.isLowLatencyMode) StudioNeonGreen else TextMuted,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Switch(
                                checked = currentEncoder.isLowLatencyMode,
                                onCheckedChange = {
                                    currentEncoder = currentEncoder.copy(isLowLatencyMode = it)
                                    onUpdateEncoder(currentEncoder)
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = StudioNeonGreen,
                                    checkedTrackColor = Color(0xFF0F3A22)
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Done Button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("done_destinations_btn")
                ) {
                    Text(
                        text = "Save & Close",
                        color = StudioObsidian,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun DestinationCard(
    destination: StreamDestination,
    isLive: Boolean,
    onToggle: () -> Unit,
    onUpdateKey: (String) -> Unit,
    onUpdateUrl: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var streamKeyInput by remember { mutableStateOf(destination.streamKey) }
    var serverUrlInput by remember { mutableStateOf(destination.serverUrl) }

    val platformColor = when (destination.platform) {
        DestinationPlatform.TWITCH -> TwitchPurple
        DestinationPlatform.YOUTUBE -> YouTubeRed
        DestinationPlatform.OK_RU -> Color(0xFFEE8208) // OK.ru Orange
        DestinationPlatform.TELEGRAM -> Color(0xFF24A1DE) // Telegram Blue
        DestinationPlatform.FACEBOOK -> FacebookBlue
        DestinationPlatform.CUSTOM_RTMPS -> RtmpsGold
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (destination.isEnabled) platformColor else StudioCardBorder
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Platform Color Dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(platformColor)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = destination.platform.platformName,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = destination.serverUrl,
                        color = TextMuted,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1
                    )
                }

                // Live Status Badge
                if (isLive && destination.isEnabled) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(StudioLiveRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIVE (${destination.latencyMs}ms)",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Switch(
                    checked = destination.isEnabled,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = platformColor,
                        checkedTrackColor = platformColor.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.testTag("toggle_dest_${destination.id}")
                )
            }

            // Stream Key and URL input toggle
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 2.dp)
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = StudioCyan,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isExpanded) "Hide Configuration" else "Edit Server URL & Stream Key",
                    color = StudioCyan,
                    fontSize = 10.sp
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = serverUrlInput,
                    onValueChange = {
                        serverUrlInput = it
                        onUpdateUrl(it)
                    },
                    label = { Text("Server URL", fontSize = 10.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xFFCBD5E1),
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = StudioCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("url_input_${destination.id}")
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = streamKeyInput,
                    onValueChange = {
                        streamKeyInput = it
                        onUpdateKey(it)
                    },
                    label = { Text("Stream Key", fontSize = 10.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xFFCBD5E1),
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = StudioCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("key_input_${destination.id}")
                )
            }
        }
    }
}
