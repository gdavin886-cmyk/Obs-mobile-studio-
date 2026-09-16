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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterVintage
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
import com.example.model.ChromaBackgroundMode
import com.example.model.ChromaKeyConfig
import com.example.ui.theme.*

@Composable
fun ChromaKeyDialog(
    config: ChromaKeyConfig,
    onSave: (ChromaKeyConfig) -> Unit,
    onDismiss: () -> Unit
) {
    var isEnabled by remember { mutableStateOf(config.isEnabled) }
    var keyColorHex by remember { mutableStateOf(config.keyColorHex) }
    var similarity by remember { mutableFloatStateOf(config.similarityThreshold) }
    var smoothness by remember { mutableFloatStateOf(config.smoothness) }
    var spillReduction by remember { mutableFloatStateOf(config.spillReduction) }
    var backgroundMode by remember { mutableStateOf(config.backgroundMode) }

    val presetColors = listOf(
        "#00FF00" to "Green Screen",
        "#0000FF" to "Blue Screen",
        "#FF00FF" to "Magenta Screen",
        "#00FF7F" to "Neon Spring"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = StudioDarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioNeonGreen),
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
                            .background(Color(0xFF0F2E1E))
                            .border(1.dp, StudioNeonGreen, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterVintage,
                            contentDescription = null,
                            tint = StudioNeonGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CHROMA KEY FILTER",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Real-Time Green Screen Background Removal",
                            color = StudioNeonGreen,
                            fontSize = 10.sp
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

                // Toggle Enable / Disable
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudioCardBg)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Enable Chroma Keying",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (isEnabled) "CURRENT: ACTIVE ($keyColorHex)" else "CURRENT: BYPASSED",
                            color = if (isEnabled) StudioNeonGreen else TextMuted,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { isEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = StudioNeonGreen,
                            checkedTrackColor = Color(0xFF0F3A22)
                        ),
                        modifier = Modifier.testTag("chroma_key_switch")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Key Color Selection
                Text(
                    text = "KEY COLOR (CURRENT: $keyColorHex)",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    presetColors.forEach { (hex, label) ->
                        val isSelected = keyColorHex.equals(hex, ignoreCase = true)
                        val color = Color(android.graphics.Color.parseColor(hex))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) StudioSurfaceVariant else StudioCardBg)
                                .border(
                                    1.dp,
                                    if (isSelected) StudioNeonGreen else StudioCardBorder,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { keyColorHex = hex }
                                .padding(vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(1.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Similarity Slider
                Text(
                    text = "CURRENT SIMILARITY: ${(similarity * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Slider(
                    value = similarity,
                    onValueChange = { similarity = it },
                    valueRange = 0.1f..0.9f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioNeonGreen,
                        activeTrackColor = StudioNeonGreen,
                        inactiveTrackColor = StudioCardBorder
                    ),
                    modifier = Modifier.testTag("chroma_similarity_slider")
                )

                // Smoothness Slider
                Text(
                    text = "CURRENT SMOOTHNESS: ${(smoothness * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Slider(
                    value = smoothness,
                    onValueChange = { smoothness = it },
                    valueRange = 0.05f..0.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioCyan,
                        activeTrackColor = StudioCyan,
                        inactiveTrackColor = StudioCardBorder
                    ),
                    modifier = Modifier.testTag("chroma_smoothness_slider")
                )

                // Key Color Spill Reduction
                Text(
                    text = "CURRENT SPILL REDUCTION: ${(spillReduction * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace
                )
                Slider(
                    value = spillReduction,
                    onValueChange = { spillReduction = it },
                    valueRange = 0.0f..0.8f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioAmber,
                        activeTrackColor = StudioAmber,
                        inactiveTrackColor = StudioCardBorder
                    ),
                    modifier = Modifier.testTag("chroma_spill_slider")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Virtual Background Mode
                Text(
                    text = "VIRTUAL BACKGROUND REPLACEMENT",
                    color = StudioCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.height(6.dp))
                ChromaBackgroundMode.values().forEach { mode ->
                    val isSelected = backgroundMode == mode
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) StudioSurfaceVariant else StudioCardBg)
                            .border(
                                1.dp,
                                if (isSelected) StudioNeonGreen else StudioCardBorder,
                                RoundedCornerShape(6.dp)
                            )
                            .clickable { backgroundMode = mode }
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { backgroundMode = mode },
                            colors = RadioButtonDefaults.colors(selectedColor = StudioNeonGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = mode.label,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(
                                config.copy(
                                    isEnabled = isEnabled,
                                    keyColorHex = keyColorHex,
                                    similarityThreshold = similarity,
                                    smoothness = smoothness,
                                    spillReduction = spillReduction,
                                    backgroundMode = backgroundMode
                                )
                            )
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StudioNeonGreen),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.testTag("save_chroma_filter_btn")
                    ) {
                        Text("Apply Chroma Filter", color = StudioObsidian, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
