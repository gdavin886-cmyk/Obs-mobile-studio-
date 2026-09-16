package com.example.ui.dialogs

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.model.*
import com.example.ui.theme.*
import com.example.util.FileUtils

@Composable
fun CustomLogoDialog(
    config: CustomLogoConfig,
    onSave: (CustomLogoConfig) -> Unit,
    onStartCountdown: (Int, String) -> Unit = { _, _ -> },
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(config.isEnabled) }
    var selectedPosition by remember { mutableStateOf(config.position) }
    var sizePercent by remember { mutableIntStateOf(config.sizePercent) }
    var opacity by remember { mutableFloatStateOf(config.opacity) }
    var watermarkText by remember { mutableStateOf(config.watermarkText) }
    var showTextLabel by remember { mutableStateOf(config.showTextLabel) }

    // Text position relative to logo {UNDER, LEFT, TOP, RIGHT}
    var textPosition by remember { mutableStateOf(config.textPosition) }

    // Frame and background customisation (Blue frame removed!)
    var showLogoFrame by remember { mutableStateOf(config.showLogoFrame) }
    var showBackground by remember { mutableStateOf(config.showBackground) }
    var backgroundColorMode by remember { mutableStateOf(config.backgroundColorMode) }
    var customBgColorHex by remember { mutableStateOf(config.customBgColorHex) }

    // Countdown setup
    var isCountdownEnabled by remember { mutableStateOf(config.isCountdownEnabled) }
    var countdownSeconds by remember { mutableIntStateOf(config.countdownTotalSeconds) }
    var countdownPosition by remember { mutableStateOf(config.countdownPosition) }
    var countdownNextText by remember { mutableStateOf(config.countdownNextText) }

    var customImageUri by remember { mutableStateOf(config.customImageUri) }
    var customImageName by remember { mutableStateOf(config.customImageName) }
    var customImageMimeType by remember { mutableStateOf(config.customImageMimeType) }

    val logoFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val queriedName = FileUtils.queryFileName(context, uri)
            val mime = context.contentResolver.getType(uri)
            customImageUri = uri.toString()
            customImageName = queriedName
            customImageMimeType = mime
            isEnabled = true
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
            border = BorderStroke(1.dp, StudioCyan),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("custom_logo_dialog")
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
                            imageVector = Icons.Default.BrandingWatermark,
                            contentDescription = null,
                            tint = StudioCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "WATERMARK LOGO & BRANDING",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Frame, Background, Text Alignment & Countdown",
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

                // Master Toggle Enable
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(StudioCardBg)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Display Watermark Logo on Stream",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isEnabled) "CURRENT: ENABLED" else "CURRENT: DISABLED",
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

                Spacer(modifier = Modifier.height(12.dp))

                // 1. BLUE FRAME REMOVAL & BACKGROUND COLOR CONTROLS
                Text(
                    text = "LOGO FRAME & BACKGROUND STYLING",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = BorderStroke(1.dp, StudioCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        // Blue Frame Remover Toggle
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Logo Frame Border",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (showLogoFrame) "Subtle Border: ON" else "Blue Frame: REMOVED (Clean Borderless)",
                                    color = if (showLogoFrame) StudioAmber else StudioNeonGreen,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Switch(
                                checked = showLogoFrame,
                                onCheckedChange = { showLogoFrame = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = StudioCyan
                                )
                            )
                        }

                        Divider(color = StudioCardBorder, modifier = Modifier.padding(vertical = 6.dp))

                        // Background On/Off Switch
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Watermark Background",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (showBackground) "Background: ACTIVE" else "Background: OFF (Fully Transparent)",
                                    color = if (showBackground) StudioNeonGreen else TextMuted,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Switch(
                                checked = showBackground,
                                onCheckedChange = { showBackground = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = StudioNeonGreen
                                )
                            )
                        }

                        if (showBackground) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Background Color Palette",
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Color preset buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                WatermarkBgColorMode.values().forEach { mode ->
                                    val isSelected = backgroundColorMode == mode
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (isSelected) StudioPurple else StudioDarkSurface)
                                            .border(
                                                1.dp,
                                                if (isSelected) StudioCyan else StudioCardBorder,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .clickable { backgroundColorMode = mode }
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = mode.label.split(" ").first(),
                                            color = if (isSelected) Color.White else TextSecondary,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. TEXT ADJUSTMENT RELATIVE TO LOGO {UNDER, LEFT, TOP, RIGHT}
                Text(
                    text = "WATERMARK NAME TEXT ADJUSTMENT",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = BorderStroke(1.dp, StudioCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        OutlinedTextField(
                            value = watermarkText,
                            onValueChange = { watermarkText = it },
                            label = { Text("Watermark Name / Channel Text", fontSize = 10.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = StudioCyan,
                                unfocusedBorderColor = StudioCardBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Position Text Relative to Logo:",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            TextRelativePosition.values().forEach { pos ->
                                val isSelected = textPosition == pos
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isSelected) StudioPurple else StudioDarkSurface)
                                        .border(
                                            1.dp,
                                            if (isSelected) StudioCyan else StudioCardBorder,
                                            RoundedCornerShape(6.dp)
                                        )
                                        .clickable { textPosition = pos }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = pos.label.replace(" Logo", "").replace("of ", ""),
                                        color = if (isSelected) Color.White else TextSecondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3. COUNTDOWN SET-UP & NEXT NAME TEXT CHANGE
                Text(
                    text = "COUNTDOWN TIMER & AUTO NEXT NAME",
                    color = StudioAmber,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = BorderStroke(1.dp, if (isCountdownEnabled) StudioAmber else StudioCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Enable Countdown Timer",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = if (isCountdownEnabled) "Auto-changes text when 00:00 reached" else "Timer currently off",
                                    color = if (isCountdownEnabled) StudioAmber else TextMuted,
                                    fontSize = 9.sp
                                )
                            }
                            Switch(
                                checked = isCountdownEnabled,
                                onCheckedChange = { isCountdownEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = StudioAmber
                                )
                            )
                        }

                        if (isCountdownEnabled) {
                            Spacer(modifier = Modifier.height(8.dp))

                            // Countdown Duration Presets
                            Text(
                                text = "Countdown Duration ($countdownSeconds seconds):",
                                color = Color.White,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf(15, 30, 60, 120, 300).forEach { sec ->
                                    val isSelected = countdownSeconds == sec
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (isSelected) StudioAmber.copy(alpha = 0.3f) else StudioDarkSurface)
                                            .border(
                                                1.dp,
                                                if (isSelected) StudioAmber else StudioCardBorder,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .clickable { countdownSeconds = sec }
                                            .padding(vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (sec < 60) "${sec}s" else "${sec / 60}m",
                                            color = if (isSelected) StudioAmber else TextSecondary,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Countdown Position {UNDER, LEFT, TOP, RIGHT, INSIDE}
                            Text(
                                text = "Countdown Placement Relative to Logo:",
                                color = Color.White,
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                CountdownPosition.values().forEach { pos ->
                                    val isSelected = countdownPosition == pos
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (isSelected) StudioAmber.copy(alpha = 0.3f) else StudioDarkSurface)
                                            .border(
                                                1.dp,
                                                if (isSelected) StudioAmber else StudioCardBorder,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .clickable { countdownPosition = pos }
                                            .padding(vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = pos.label.split(" ").first(),
                                            color = if (isSelected) StudioAmber else TextSecondary,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Automatic Next Name Text
                            OutlinedTextField(
                                value = countdownNextText,
                                onValueChange = { countdownNextText = it },
                                label = { Text("Next Name / Text (When countdown ends)", fontSize = 10.sp) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = StudioAmber,
                                    unfocusedBorderColor = StudioCardBorder,
                                    focusedLabelColor = StudioAmber
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 4. SCREEN CORNER POSITION & SIZE
                Text(
                    text = "SCREEN CORNER POSITION",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PositionButton(
                        label = "TOP-LEFT",
                        isSelected = selectedPosition == LogoPosition.TOP_LEFT,
                        onClick = { selectedPosition = LogoPosition.TOP_LEFT },
                        modifier = Modifier.weight(1f)
                    )
                    PositionButton(
                        label = "TOP-RIGHT",
                        isSelected = selectedPosition == LogoPosition.TOP_RIGHT,
                        onClick = { selectedPosition = LogoPosition.TOP_RIGHT },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PositionButton(
                        label = "BOTTOM-LEFT",
                        isSelected = selectedPosition == LogoPosition.BOTTOM_LEFT,
                        onClick = { selectedPosition = LogoPosition.BOTTOM_LEFT },
                        modifier = Modifier.weight(1f)
                    )
                    PositionButton(
                        label = "BOTTOM-RIGHT",
                        isSelected = selectedPosition == LogoPosition.BOTTOM_RIGHT,
                        onClick = { selectedPosition = LogoPosition.BOTTOM_RIGHT },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Custom Logo File Picker (.PNG / .SVG / .JPG / .GIF)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { logoFilePickerLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = StudioObsidian,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (customImageUri != null) "Change Image" else "Upload Custom Logo",
                            color = StudioObsidian,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (customImageUri != null) {
                        OutlinedButton(
                            onClick = {
                                customImageUri = null
                                customImageName = null
                                customImageMimeType = null
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = StudioRecRed),
                            border = BorderStroke(1.dp, StudioRecRed),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Reset", color = StudioRecRed, fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Apply Button
                Button(
                    onClick = {
                        val updated = config.copy(
                            isEnabled = isEnabled,
                            position = selectedPosition,
                            sizePercent = sizePercent,
                            opacity = opacity,
                            watermarkText = watermarkText,
                            showTextLabel = showTextLabel,
                            textPosition = textPosition,
                            showLogoFrame = showLogoFrame,
                            showBackground = showBackground,
                            backgroundColorMode = backgroundColorMode,
                            customBgColorHex = customBgColorHex,
                            isCountdownEnabled = isCountdownEnabled,
                            countdownTotalSeconds = countdownSeconds,
                            countdownRemainingSeconds = countdownSeconds,
                            countdownPosition = countdownPosition,
                            countdownNextText = countdownNextText,
                            isCountdownRunning = isCountdownEnabled,
                            customImageUri = customImageUri,
                            customImageName = customImageName,
                            customImageMimeType = customImageMimeType
                        )
                        onSave(updated)
                        if (isCountdownEnabled) {
                            onStartCountdown(countdownSeconds, countdownNextText)
                        }
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("save_logo_btn")
                ) {
                    Text(
                        text = "Apply Watermark & Styling",
                        color = StudioObsidian,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PositionButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) StudioPurple else StudioCardBg)
            .border(
                1.dp,
                if (isSelected) StudioCyan else StudioCardBorder,
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}
