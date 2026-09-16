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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
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
import com.example.model.MarqueeMod
import com.example.model.MarqueeSpeed
import com.example.model.ScrollingTextConfig
import com.example.ui.theme.*

@Composable
fun ScrollingTextDialog(
    config: ScrollingTextConfig,
    isLive: Boolean,
    onSave: (ScrollingTextConfig) -> Unit,
    onDismiss: () -> Unit
) {
    var isEnabled by remember { mutableStateOf(config.isEnabled) }
    var isPermanentRunning by remember { mutableStateOf(config.isPermanentRunning) }
    var textContent by remember { mutableStateOf(config.textContent) }
    var selectedMod by remember { mutableStateOf(config.mod) }
    var selectedSpeed by remember { mutableStateOf(config.speed) }
    var showPrefixBadge by remember { mutableStateOf(config.showPrefixBadge) }
    var backgroundColorHex by remember { mutableStateOf(config.backgroundColorHex) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
            border = BorderStroke(1.dp, StudioCyan),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("scrolling_text_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
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
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = StudioCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SCROLLING TEXT TICKER",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Broadcast News, Inform & Crawl Marquee",
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

                // Primary Master Controls: Ticker Enabled & Permanent Running
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = BorderStroke(1.dp, StudioCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        // Ticker Master On/Off
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Enable Scrolling Ticker",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isEnabled) "STATUS: RUNNING ACTIVE" else "STATUS: TURNED OFF",
                                    color = if (isEnabled) StudioNeonGreen else TextMuted,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Switch(
                                checked = isEnabled,
                                onCheckedChange = { isEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = StudioNeonGreen
                                )
                            )
                        }

                        Divider(
                            color = StudioCardBorder,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // Permanent Running Live Mode
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Permanent Running Mode",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (isPermanentRunning)
                                        "ACTIVE: Always scrolls non-stop (Preview & Live)"
                                    else
                                        "LIVE SYNC: Automatically runs when Live, stops when Live ends",
                                    color = if (isPermanentRunning) StudioCyan else TextSecondary,
                                    fontSize = 9.sp,
                                    lineHeight = 12.sp
                                )
                            }
                            Switch(
                                checked = isPermanentRunning,
                                onCheckedChange = { isPermanentRunning = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = StudioCyan
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Marquee Animation Mod Presets
                Text(
                    text = "ANIMATION MOD PRESETS",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    MarqueeMod.values().forEach { mod ->
                        val isSelected = selectedMod == mod
                        val modBadgeColor = try {
                            Color(android.graphics.Color.parseColor(mod.badgeColorHex))
                        } catch (_: Exception) {
                            StudioCyan
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) modBadgeColor.copy(alpha = 0.3f) else StudioCardBg)
                                .border(
                                    1.dp,
                                    if (isSelected) modBadgeColor else StudioCardBorder,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable {
                                    selectedMod = mod
                                }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = mod.label,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Text Content Input
                Text(
                    text = "TICKER TEXT CONTENT",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = textContent,
                    onValueChange = { textContent = it },
                    label = { Text("News or Broadcasting Inform Text", fontSize = 10.sp) },
                    placeholder = { Text("Type ticker updates or breaking news here...", fontSize = 11.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = StudioCardBorder,
                        focusedLabelColor = StudioCyan
                    ),
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ticker_text_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Crawling Speed Controls
                Text(
                    text = "ANIMATION SCROLL SPEED",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MarqueeSpeed.values().forEach { speed ->
                        val isSelected = selectedSpeed == speed
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) StudioPurple.copy(alpha = 0.35f) else StudioCardBg)
                            .border(
                                1.dp,
                                if (isSelected) StudioPurple else StudioCardBorder,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { selectedSpeed = speed }
                            .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = speed.label,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Show Prefix Tag Badge Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(StudioCardBg)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Display Category Badge Tag (${selectedMod.prefixTag})",
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = showPrefixBadge,
                        onCheckedChange = { showPrefixBadge = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = StudioCyan
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                        border = BorderStroke(1.dp, StudioCardBorder),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", fontSize = 11.sp)
                    }

                    Button(
                        onClick = {
                            onSave(
                                config.copy(
                                    isEnabled = isEnabled,
                                    isPermanentRunning = isPermanentRunning,
                                    textContent = textContent,
                                    mod = selectedMod,
                                    speed = selectedSpeed,
                                    showPrefixBadge = showPrefixBadge,
                                    backgroundColorHex = backgroundColorHex
                                )
                            )
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Apply Ticker",
                            color = StudioObsidian,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
