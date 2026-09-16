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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrandingWatermark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.RestartAlt
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
import com.example.model.CustomLogoConfig
import com.example.model.LogoPosition
import com.example.ui.theme.*
import com.example.util.FileUtils

@Composable
fun CustomLogoDialog(
    config: CustomLogoConfig,
    onSave: (CustomLogoConfig) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isEnabled by remember { mutableStateOf(config.isEnabled) }
    var selectedPosition by remember { mutableStateOf(config.position) }
    var sizePercent by remember { mutableIntStateOf(config.sizePercent) }
    var opacity by remember { mutableFloatStateOf(config.opacity) }
    var watermarkText by remember { mutableStateOf(config.watermarkText) }
    var showTextLabel by remember { mutableStateOf(config.showTextLabel) }

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
                            text = "CUSTOM LOGO WATERMARK",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Channel Branding & Watermark Overlay",
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

                Spacer(modifier = Modifier.height(14.dp))

                // Toggle Enable
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
                            text = "Display Custom Logo on Stream",
                            color = Color.White,
                            fontSize = 12.sp
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
                            checkedThumbColor = StudioCyan,
                            checkedTrackColor = StudioSurfaceVariant
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // CUSTOM LOGO FILE UPLOAD SECTION (.PNG, .SVG, .JPG, .GIF)
                Text(
                    text = "LOGO FILE UPLOAD (.PNG / .SVG / .JPG / .GIF)",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = StudioCardBg),
                    border = BorderStroke(1.dp, if (customImageUri != null) StudioNeonGreen else StudioCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Logo Preview Thumbnail
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF0F172A))
                                    .border(1.dp, StudioCyan, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!customImageUri.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(context)
                                            .data(customImageUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Uploaded Logo",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(4.dp)
                                    )
                                } else {
                                    Image(
                                        painter = painterResource(id = R.drawable.obs_studio_logo_1789472344798),
                                        contentDescription = "Default Logo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                val badgeText = FileUtils.getFormatBadge(customImageName, customImageMimeType)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(if (customImageUri != null) StudioNeonGreen else StudioPurple)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = badgeText,
                                            color = StudioObsidian,
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (customImageUri != null) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = StudioNeonGreen,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "ACTIVE",
                                            color = StudioNeonGreen,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = customImageName ?: "obs_studio_official_logo.png",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "Supported: .png, .svg, .jpg, .gif",
                                    color = TextSecondary,
                                    fontSize = 9.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = {
                                    logoFilePickerLauncher.launch("image/*")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .testTag("upload_logo_file_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudUpload,
                                    contentDescription = null,
                                    tint = StudioObsidian,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Upload Logo File",
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
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .height(40.dp)
                                        .testTag("reset_logo_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RestartAlt,
                                        contentDescription = "Reset",
                                        tint = StudioRecRed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Reset",
                                        color = StudioRecRed,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Logo Position Picker (2x2 grid representing 4 corners)
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

                // Size Slider
                Text(
                    text = "CURRENT LOGO SIZE: $sizePercent% (OF SCREEN)",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Slider(
                    value = sizePercent.toFloat(),
                    onValueChange = { sizePercent = it.toInt() },
                    valueRange = 10f..40f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioCyan,
                        activeTrackColor = StudioCyan,
                        inactiveTrackColor = StudioCardBorder
                    )
                )

                // Opacity Slider
                Text(
                    text = "CURRENT OPACITY: ${(opacity * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Slider(
                    value = opacity,
                    onValueChange = { opacity = it },
                    valueRange = 0.2f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioPurple,
                        activeTrackColor = StudioPurple,
                        inactiveTrackColor = StudioCardBorder
                    )
                )

                // Watermark Text
                OutlinedTextField(
                    value = watermarkText,
                    onValueChange = { watermarkText = it },
                    label = { Text("Watermark Text Label", fontSize = 10.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color(0xFFCBD5E1),
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = StudioCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Save button
                Button(
                    onClick = {
                        onSave(
                            config.copy(
                                isEnabled = isEnabled,
                                position = selectedPosition,
                                sizePercent = sizePercent,
                                opacity = opacity,
                                watermarkText = watermarkText,
                                showTextLabel = showTextLabel,
                                customImageUri = customImageUri,
                                customImageName = customImageName,
                                customImageMimeType = customImageMimeType
                            )
                        )
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
                        text = "Apply Custom Logo Overlay",
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
