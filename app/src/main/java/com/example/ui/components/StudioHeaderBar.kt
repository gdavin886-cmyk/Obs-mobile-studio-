package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.StudioTelemetry
import com.example.ui.theme.*

@Composable
fun StudioHeaderBar(
    isLive: Boolean,
    isRecording: Boolean,
    isStudioMode: Boolean,
    durationSeconds: Long,
    telemetry: StudioTelemetry,
    onToggleLive: () -> Unit,
    onToggleRecording: () -> Unit,
    onToggleStudioMode: () -> Unit,
    onOpenDestinations: () -> Unit,
    onOpenAlertSimulator: () -> Unit,
    modifier: Modifier = Modifier
) {
    val durationFormatted = remember(durationSeconds) {
        val hours = durationSeconds / 3600
        val minutes = (durationSeconds % 3600) / 60
        val seconds = durationSeconds % 60
        String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    val livePillColor by animateColorAsState(
        targetValue = if (isLive) StudioLiveRed else StudioCardBg,
        label = "live_color"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "header_pulse")
    val liveDotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "live_dot"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(StudioDarkSurface)
            .border(1.dp, StudioCardBorder)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Row 1: Brand, Status, and Utility Actions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // App Icon & Brand
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, StudioCyan, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.obs_studio_logo_1789472344798),
                        contentDescription = "OBS Studio Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "OBS STUDIO",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(StudioPurple)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "MOBILE",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isLive) StudioLiveRed.copy(alpha = liveDotAlpha)
                                    else TextMuted
                                )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isLive) "CURRENT: LIVE $durationFormatted" else "CURRENT: STANDBY",
                            color = if (isLive) StudioNeonGreen else TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Quick Engagement Alert Trigger Icon
            IconButton(
                onClick = onOpenAlertSimulator,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(StudioCardBg)
                    .border(1.dp, StudioAmber.copy(alpha = 0.6f), CircleShape)
                    .testTag("open_alert_simulator_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = "Viewer Engagement Alerts",
                    tint = StudioAmber,
                    modifier = Modifier.size(17.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Multi-Destination Manager Button
            IconButton(
                onClick = onOpenDestinations,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(StudioCardBg)
                    .border(1.dp, StudioCyan.copy(alpha = 0.6f), CircleShape)
                    .testTag("open_destinations_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = "Stream Output Destinations",
                    tint = StudioCyan,
                    modifier = Modifier.size(17.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2: Mobile Action Controls (Studio Mode, REC, GO LIVE)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Studio Mode Switch
            FilterChip(
                selected = isStudioMode,
                onClick = onToggleStudioMode,
                label = {
                    Text(
                        text = if (isStudioMode) "STUDIO: ON" else "STUDIO: OFF",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StudioPurple,
                    selectedLabelColor = Color.White,
                    containerColor = StudioCardBg,
                    labelColor = TextSecondary
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isStudioMode,
                    borderColor = if (isStudioMode) StudioPurple else StudioCardBorder
                ),
                modifier = Modifier
                    .height(34.dp)
                    .testTag("toggle_studio_mode_btn")
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Record Button
            Button(
                onClick = onToggleRecording,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecording) StudioRecRed else StudioCardBg
                ),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isRecording) StudioRecRed else StudioCardBorder
                ),
                modifier = Modifier
                    .height(34.dp)
                    .testTag("toggle_rec_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isRecording) Color.White else StudioRecRed)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (isRecording) "RECORDING" else "REC",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = if (isRecording) Color.White else TextPrimary
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // GO LIVE Button (Primary broadcast action, flexible width)
            Button(
                onClick = onToggleLive,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLive) StudioLiveRed else StudioCyan
                ),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
                    .testTag("toggle_live_btn")
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isLive) Color.White else StudioObsidian)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isLive) "STOP LIVE" else "GO LIVE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    color = if (isLive) Color.White else StudioObsidian
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 3: Mobile Telemetry & Real-Time Stats Strip (Horizontally Scrollable so nothing clips)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(StudioObsidian)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            TelemetryItem(
                label = "FPS",
                value = "${telemetry.fps}",
                color = if (telemetry.fps >= 59) StudioNeonGreen else StudioAmber
            )
            Box(modifier = Modifier.size(1.dp, 10.dp).background(StudioCardBorder))
            TelemetryItem(
                label = "BITRATE",
                value = if (isLive) "${telemetry.bitrateKbps} kbps" else "0 kbps (IDLE)",
                color = StudioCyan
            )
            Box(modifier = Modifier.size(1.dp, 10.dp).background(StudioCardBorder))
            TelemetryItem(
                label = "DROP",
                value = String.format("%.2f%%", telemetry.droppedFramesPercent),
                color = if (telemetry.droppedFramesPercent < 0.1f) StudioNeonGreen else StudioLiveRed
            )
            Box(modifier = Modifier.size(1.dp, 10.dp).background(StudioCardBorder))
            TelemetryItem(
                label = "LATENCY",
                value = if (isLive) "${telemetry.rtmpsLatencyMs} ms" else "--",
                color = StudioNeonGreen
            )
            Box(modifier = Modifier.size(1.dp, 10.dp).background(StudioCardBorder))
            TelemetryItem(
                label = "ENCODER",
                value = if (telemetry.hardwareEncoderEngaged) "HW-MediaCodec" else "SW-x264",
                color = StudioPurple
            )
            Box(modifier = Modifier.size(1.dp, 10.dp).background(StudioCardBorder))
            TelemetryItem(
                label = "CPU",
                value = "${telemetry.cpuUsagePercent}%",
                color = if (telemetry.cpuUsagePercent < 50) TextSecondary else StudioAmber
            )
        }
    }
}

@Composable
private fun TelemetryItem(label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "$label: ",
            color = TextMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = value,
            color = color,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
