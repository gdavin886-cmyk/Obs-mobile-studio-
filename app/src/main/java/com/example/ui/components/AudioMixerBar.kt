package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AudioTrack
import com.example.ui.theme.*

@Composable
fun AudioMixerBar(
    tracks: List<AudioTrack>,
    onVolumeChange: (String, Float) -> Unit,
    onToggleMute: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(StudioDarkSurface)
            .border(1.dp, StudioCardBorder)
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "AUDIO MIXER",
                color = StudioCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "AAC 48kHz Stereo • Hardware Bus",
                color = TextMuted,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Grid of Audio Channels
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            tracks.forEach { track ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(StudioCardBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    // Mute Button with comfortable touch target
                    IconButton(
                        onClick = { onToggleMute(track.id) },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (track.isMuted) StudioLiveRed else StudioDarkSurface)
                            .border(
                                1.dp,
                                if (track.isMuted) StudioLiveRed else StudioCardBorder,
                                RoundedCornerShape(6.dp)
                            )
                            .testTag("mute_btn_${track.id}")
                    ) {
                        Icon(
                            imageVector = if (track.isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                            contentDescription = "Toggle Mute",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Channel Name & Current Levels
                    Column(modifier = Modifier.width(82.dp)) {
                        Text(
                            text = track.name,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (track.isMuted) "MUTED" else String.format("%.1f dB", track.peakDb),
                                color = if (track.isMuted) StudioLiveRed else StudioNeonGreen,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${(track.volume * 100).toInt()}%",
                                color = TextMuted,
                                fontSize = 8.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Dynamic VU Meter Bar + Slider
                    Column(modifier = Modifier.weight(1f)) {
                        // VU Meter Graphic
                        val meterFraction = if (track.isMuted) 0f else {
                            // Map -60dB -> 0dB to 0.0 -> 1.0
                            ((track.peakDb + 60f) / 60f).coerceIn(0f, 1f)
                        }

                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(StudioObsidian)
                        ) {
                            val activeWidth = size.width * meterFraction
                            drawRect(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MeterGreen,
                                        MeterGreen,
                                        MeterYellow,
                                        MeterRed
                                    )
                                ),
                                size = androidx.compose.ui.geometry.Size(activeWidth, size.height)
                            )
                        }

                        // Compact Fader
                        Slider(
                            value = track.volume,
                            onValueChange = { onVolumeChange(track.id, it) },
                            valueRange = 0f..1.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = StudioCyan,
                                activeTrackColor = StudioCyan,
                                inactiveTrackColor = StudioCardBorder
                            ),
                            modifier = Modifier
                                .height(22.dp)
                                .fillMaxWidth()
                                .testTag("fader_${track.id}")
                        )
                    }
                }
            }
        }
    }
}
