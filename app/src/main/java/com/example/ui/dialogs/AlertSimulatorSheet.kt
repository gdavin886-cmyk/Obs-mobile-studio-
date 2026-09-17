package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
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
import com.example.model.AlertType
import com.example.ui.theme.*

@Composable
fun AlertSimulatorSheet(
    onTriggerAlert: (AlertType, String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var customName by remember { mutableStateOf("NeonRider99") }
    var customAmount by remember { mutableStateOf("$50.00") }
    var customMessage by remember { mutableStateOf("Huge fan of the broadcast! Keep crushing it!") }
    var selectedType by remember { mutableStateOf(AlertType.SUPER_CHAT) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioAmber),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
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
                            .border(1.dp, StudioAmber, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = StudioAmber,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "VIEWER ENGAGEMENT ALERTS",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Pop-up Animated Overlays & Event Triggers",
                            color = StudioAmber,
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

                Spacer(modifier = Modifier.height(14.dp))

                // Quick Event Presets
                Text(
                    text = "QUICK TEST TRIGGERS",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    QuickTriggerButton(
                        title = "New Follower",
                        subtitle = "Alex_Streams just followed!",
                        badge = "+1 FOLLOW",
                        badgeColor = StudioCyan,
                        onClick = {
                            onTriggerAlert(
                                AlertType.FOLLOWER,
                                "Alex_Streams",
                                "",
                                "Welcome to the stream channel!"
                            )
                            onDismiss()
                        }
                    )

                    QuickTriggerButton(
                        title = "Subscriber (Tier 1)",
                        subtitle = "CyberValkyrie renewed for 6 months!",
                        badge = "SUB!",
                        badgeColor = TwitchPurple,
                        onClick = {
                            onTriggerAlert(
                                AlertType.SUBSCRIBER,
                                "CyberValkyrie",
                                "Tier 1 (6 Months)",
                                "Can't wait for today's gameplay session!"
                            )
                            onDismiss()
                        }
                    )

                    QuickTriggerButton(
                        title = "Super Chat / Donation",
                        subtitle = "GamerGod donated $100.00!",
                        badge = "$100 DONATION",
                        badgeColor = StudioAmber,
                        onClick = {
                            onTriggerAlert(
                                AlertType.SUPER_CHAT,
                                "GamerGod_88",
                                "$100.00",
                                "Let's goooo! Amazing stream quality on mobile!"
                            )
                            onDismiss()
                        }
                    )

                    QuickTriggerButton(
                        title = "Incoming Raid",
                        subtitle = "TechnoKnight raided with 350 viewers!",
                        badge = "350 RAIDERS",
                        badgeColor = StudioLiveRed,
                        onClick = {
                            onTriggerAlert(
                                AlertType.RAID,
                                "TechnoKnight",
                                "350 Viewers",
                                "Raiding from our broadcast! Show some love!"
                            )
                            onDismiss()
                        }
                    )

                    QuickTriggerButton(
                        title = "Community Gift Subs",
                        subtitle = "PixelQueen gifted 10 subs!",
                        badge = "10 GIFT SUBS",
                        badgeColor = StudioNeonGreen,
                        onClick = {
                            onTriggerAlert(
                                AlertType.GIFT_SUB,
                                "PixelQueen",
                                "x10 Subscriptions",
                                "Hype in the chat for everyone!"
                            )
                            onDismiss()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Custom Engagement Event Builder
                Text(
                    text = "CUSTOM VIEWER EVENT BUILDER (CURRENT: ${selectedType.title})",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Event Type Selector Chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    AlertType.values().forEach { type ->
                        val isSel = selectedType == type
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSel) StudioAmber else StudioCardBg)
                                .border(
                                    1.dp,
                                    if (isSel) StudioAmber else StudioCardBorder,
                                    RoundedCornerShape(4.dp)
                                )
                                .clickable { selectedType = type }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = type.title,
                                color = if (isSel) StudioObsidian else TextSecondary,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = customName,
                    onValueChange = { customName = it },
                    label = { Text("Viewer Handle / Username", fontSize = 10.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xFFCBD5E1),
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = StudioCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = customAmount,
                    onValueChange = { customAmount = it },
                    label = { Text("Amount / Tier / Raid Details", fontSize = 10.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xFFCBD5E1),
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = StudioCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = customMessage,
                    onValueChange = { customMessage = it },
                    label = { Text("Donor Message", fontSize = 10.sp) },
                    maxLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xFFCBD5E1),
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = StudioCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        onTriggerAlert(
                            selectedType,
                            customName,
                            customAmount,
                            customMessage
                        )
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioAmber),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("trigger_custom_alert_btn")
                ) {
                    Text(
                        text = "Trigger Pop-Up Overlay On Stream",
                        color = StudioObsidian,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickTriggerButton(
    title: String,
    subtitle: String,
    badge: String,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(StudioCardBg)
            .border(0.5.dp, StudioCardBorder, RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = TextSecondary,
                fontSize = 9.sp
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(badgeColor)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = badge,
                color = StudioObsidian,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
