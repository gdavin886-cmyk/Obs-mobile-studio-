package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.style.TextOverflow
import com.example.model.SceneId
import com.example.model.StudioScene
import com.example.model.TransitionType
import com.example.ui.theme.*

@Composable
fun SceneSwitcherBar(
    scenes: List<StudioScene>,
    activeScene: StudioScene,
    previewScene: StudioScene,
    isStudioMode: Boolean,
    selectedTransition: TransitionType,
    transitionDurationMs: Int,
    onSelectScene: (StudioScene) -> Unit,
    onTransitionStudio: () -> Unit,
    onSelectTransition: (TransitionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(StudioDarkSurface)
            .border(1.dp, StudioCardBorder)
            .padding(8.dp)
    ) {
        // Section Header with Transition Selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isStudioMode) "SCENE SWITCHER (STUDIO MODE)" else "SCENE SWITCHER",
                    color = if (isStudioMode) StudioAmber else StudioCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = if (isStudioMode) "CURRENT: Staging in Preview" else "CURRENT: Direct Switch",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
            }

            // Transition Type Selectors
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TransitionType.values().forEach { trans ->
                    val isSelected = selectedTransition == trans
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isSelected) StudioPurple else StudioCardBg)
                            .border(
                                1.dp,
                                if (isSelected) StudioPurple else StudioCardBorder,
                                RoundedCornerShape(4.dp)
                            )
                            .clickable { onSelectTransition(trans) }
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = trans.name,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // Studio Mode Full-Width Transition Button (Ergonomic thumb trigger)
        if (isStudioMode) {
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = onTransitionStudio,
                colors = ButtonDefaults.buttonColors(containerColor = StudioLiveRed),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .testTag("studio_transition_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Transform,
                    contentDescription = "Cut to Program",
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "TRANSITION: ${selectedTransition.name} TO LIVE PROGRAM",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Horizontal Carousel of Scenes
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            scenes.forEach { scene ->
                val isProgram = scene.id == activeScene.id
                val isPreview = isStudioMode && scene.id == previewScene.id

                val borderColor = when {
                    isProgram -> StudioLiveRed
                    isPreview -> StudioAmber
                    else -> StudioCardBorder
                }

                val tagColor = when {
                    isProgram -> StudioLiveRed
                    isPreview -> StudioAmber
                    else -> Color.Transparent
                }

                val icon: ImageVector = when (scene.id) {
                    SceneId.CAMERA_GREENSCREEN -> Icons.Default.Videocam
                    SceneId.SCREEN_CAST -> Icons.Default.ScreenShare
                    SceneId.WEB_CAST -> Icons.Default.Language
                    SceneId.MEDIA_CAST -> Icons.Default.VideoLibrary
                    SceneId.STARTING_SOON -> Icons.Default.HourglassTop
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isProgram) StudioCardBg else StudioObsidian)
                        .border(1.5.dp, borderColor, RoundedCornerShape(8.dp))
                        .clickable { onSelectScene(scene) }
                        .padding(horizontal = 6.dp, vertical = 8.dp)
                        .testTag("scene_card_${scene.id.name}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(StudioDarkSurface)
                            .border(1.dp, StudioCardBorder, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = scene.name,
                            tint = when {
                                isProgram -> StudioLiveRed
                                isPreview -> StudioAmber
                                else -> StudioCyan
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = scene.name,
                        color = if (isProgram || isPreview) Color.White else TextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    // Status pill with explicit CURRENT status
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(if (isProgram || isPreview) tagColor else Color(0x33FFFFFF))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = when {
                                isProgram -> "CURRENT: LIVE"
                                isPreview -> "CURRENT: PREVIEW"
                                else -> "STANDBY"
                            },
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
