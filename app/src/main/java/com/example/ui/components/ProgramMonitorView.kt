package com.example.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun ProgramMonitorView(
    scene: StudioScene,
    isLive: Boolean,
    isRecording: Boolean,
    isPreview: Boolean = false,
    chromaKeyConfig: ChromaKeyConfig,
    customLogoConfig: CustomLogoConfig,
    activeAlert: EngagementAlert?,
    onDismissAlert: () -> Unit,
    isFrontCamera: Boolean,
    isTorchOn: Boolean,
    webCastUrl: String,
    onWebUrlChange: (String) -> Unit,
    mediaIsPlaying: Boolean,
    onToggleMediaPlay: () -> Unit,
    mediaCastConfig: CustomMediaCastConfig = CustomMediaCastConfig(),
    onUploadMediaClick: () -> Unit = {},
    onClearMediaClick: () -> Unit = {},
    scrollingTextConfig: ScrollingTextConfig? = null,
    onToggleScrollingText: () -> Unit = {},
    onOpenScrollingTextDialog: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Studio Tally Border: Red when Live, Amber when in Preview mode, Cyan/Green when idle
    val tallyColor = when {
        isLive -> StudioLiveRed
        isPreview -> StudioAmber
        else -> StudioCyan.copy(alpha = 0.5f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black)
            .border(2.dp, tallyColor, RoundedCornerShape(10.dp))
            .testTag(if (isPreview) "preview_monitor_view" else "program_monitor_view")
    ) {
        // Render Active Scene Content with smooth Crossfade
        Crossfade(
            targetState = scene.id,
            animationSpec = tween(280),
            label = "scene_crossfade"
        ) { sceneId ->
            when (sceneId) {
                SceneId.CAMERA_GREENSCREEN -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (chromaKeyConfig.isEnabled) {
                            // Virtual Studio Room background composited under camera
                            when (chromaKeyConfig.backgroundMode) {
                                ChromaBackgroundMode.VIRTUAL_STUDIO -> {
                                    Image(
                                        painter = painterResource(id = R.drawable.studio_virtual_bg_1789472361323),
                                        contentDescription = "Virtual Studio Background",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                ChromaBackgroundMode.SOLID_DARK -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(StudioDarkSurface)
                                    )
                                }
                                ChromaBackgroundMode.TRANSPARENT_COMPOSITE -> {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color(0xFF0F172A))
                                    )
                                }
                            }

                            // Camera layer with green screen keying simulation
                            CameraPreviewView(
                                modifier = Modifier.fillMaxSize(),
                                isFrontCamera = isFrontCamera,
                                isTorchOn = isTorchOn,
                                isChromaKeyEnabled = true,
                                keyColorHex = chromaKeyConfig.keyColorHex
                            )

                            // Chroma Key Status Tag
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xCC000000))
                                    .border(1.dp, StudioNeonGreen, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(StudioNeonGreen)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "CHROMA KEY ON: ${chromaKeyConfig.keyColorHex} (${(chromaKeyConfig.similarityThreshold * 100).toInt()}%)",
                                    color = StudioNeonGreen,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        } else {
                            // Direct camera feed without keying
                            CameraPreviewView(
                                modifier = Modifier.fillMaxSize(),
                                isFrontCamera = isFrontCamera,
                                isTorchOn = isTorchOn,
                                isChromaKeyEnabled = false
                            )
                        }
                    }
                }

                SceneId.SCREEN_CAST -> {
                    ScreenCastSimulatorView(
                        modifier = Modifier.fillMaxSize(),
                        isPipFacecamVisible = true,
                        isFrontCamera = isFrontCamera,
                        isTorchOn = isTorchOn
                    )
                }

                SceneId.WEB_CAST -> {
                    WebCastSourceView(
                        url = webCastUrl,
                        modifier = Modifier.fillMaxSize(),
                        onUrlChange = onWebUrlChange
                    )
                }

                SceneId.MEDIA_CAST -> {
                    MediaCastSourceView(
                        modifier = Modifier.fillMaxSize(),
                        mediaConfig = mediaCastConfig,
                        isPlaying = mediaIsPlaying,
                        onTogglePlay = onToggleMediaPlay,
                        onUploadClick = onUploadMediaClick,
                        onClearClick = onClearMediaClick
                    )
                }

                SceneId.STARTING_SOON -> {
                    StartingSoonSourceView(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        // Custom Logo Watermark
        CustomLogoWatermarkView(config = customLogoConfig)

        // Scrolling News / Broadcasting Inform Text Ticker (Pinned along bottom)
        if (scrollingTextConfig != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                ScrollingTextTickerView(
                    config = scrollingTextConfig,
                    isLive = isLive,
                    onToggleEnabled = onToggleScrollingText,
                    onOpenEditDialog = onOpenScrollingTextDialog
                )
            }
        }

        // Pop-up Engagement Alert Box Overlay (Triggered by viewer engagement)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp)
        ) {
            EngagementAlertBoxView(
                alert = activeAlert,
                onDismiss = onDismissAlert
            )
        }

        // Tally Pill (Top Left)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xDD000000))
                .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(tallyColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = when {
                    isLive -> "CURRENT: LIVE ON AIR"
                    isPreview -> "CURRENT: PREVIEW (STAGED)"
                    else -> "CURRENT: PROGRAM READY"
                },
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            if (isRecording) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(StudioRecRed)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "REC",
                    color = StudioRecRed,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Scene Name Watermark (Bottom Right)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xAA000000))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = "CURRENT: ${scene.name.uppercase()}",
                color = Color(0xFFCBD5E1),
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
