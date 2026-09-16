package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.R

enum class SceneId(val title: String, val iconName: String) {
    CAMERA_GREENSCREEN("Camera + Chroma", "Videocam"),
    SCREEN_CAST("Screen + Facecam", "ScreenShare"),
    WEB_CAST("Website Cast", "Language"),
    MEDIA_CAST("Media / Video File", "VideoLibrary"),
    STARTING_SOON("Starting / BRB", "HourglassTop")
}

enum class SourceType(val displayName: String) {
    CAMERA("Camera Feed"),
    SCREEN("Mobile Screen Capture"),
    WEB_PAGE("Web Browser"),
    FILE_MEDIA("Video / Image File"),
    CUSTOM_LOGO("Custom Logo Watermark"),
    ALERT_OVERLAY("Engagement Alerts Box")
}

data class SourceItem(
    val id: String,
    val type: SourceType,
    val name: String,
    val isVisible: Boolean = true,
    val isLocked: Boolean = false,
    val zIndex: Int = 0
)

data class StudioScene(
    val id: SceneId,
    val name: String,
    val sources: List<SourceItem>
)

enum class DestinationPlatform(val platformName: String, val defaultUrl: String) {
    TWITCH("Twitch", "rtmps://live.twitch.tv/app/"),
    YOUTUBE("YouTube Live", "rtmps://a.rtmp.youtube.com/live2"),
    FACEBOOK("Facebook Live", "rtmps://live-api-s.facebook.com:443/rtmp/"),
    CUSTOM_RTMPS("Custom RTMPS", "rtmps://custom.server.com:443/live/")
}

data class StreamDestination(
    val id: String,
    val platform: DestinationPlatform,
    val serverUrl: String,
    val streamKey: String,
    val isEnabled: Boolean = true,
    val isLive: Boolean = false,
    val currentBitrateKbps: Int = 0,
    val latencyMs: Int = 0,
    val droppedFramesPercent: Float = 0.0f
)

enum class HardwareCodec(val codecName: String, val chipVendor: String, val isHardware: Boolean) {
    H264_MEDIACODEC("H.264 / AVC (Hardware)", "Qualcomm / MediaTek MediaCodec", true),
    HEVC_MEDIACODEC("H.265 / HEVC (Hardware)", "Snapdragon / Tensor MediaCodec", true),
    AV1_MEDIACODEC("AV1 (Hardware Next-Gen)", "Hardware MediaCodec AV1", true),
    X264_SOFTWARE("x264 (Software CPU)", "Standard Libav / FFmpeg", false)
}

enum class StreamResolution(val label: String, val width: Int, val height: Int, val defaultBitrate: Int) {
    RES_1080P_60("1080p 60fps (Full HD)", 1920, 1080, 6000),
    RES_1080P_30("1080p 30fps", 1920, 1080, 4500),
    RES_720P_60("720p 60fps (HD Fast)", 1280, 720, 3500),
    RES_720P_30("720p 30fps (Low Bandwidth)", 1280, 720, 2500),
    RES_4K_30("4K 2160p 30fps (Ultra HD)", 3840, 2160, 12000)
}

data class EncoderConfig(
    val codec: HardwareCodec = HardwareCodec.H264_MEDIACODEC,
    val resolution: StreamResolution = StreamResolution.RES_1080P_60,
    val targetBitrateKbps: Int = 6000,
    val fps: Int = 60,
    val isLowLatencyMode: Boolean = true,
    val keyframeIntervalSec: Int = 2,
    val audioBitrateKbps: Int = 160
)

enum class ChromaBackgroundMode(val label: String) {
    VIRTUAL_STUDIO("Cyberpunk Neon Studio"),
    TRANSPARENT_COMPOSITE("Transparent (Overlay over scene)"),
    SOLID_DARK("Solid Broadcast Charcoal")
}

data class ChromaKeyConfig(
    val isEnabled: Boolean = true,
    val keyColorHex: String = "#00FF00", // Green screen
    val similarityThreshold: Float = 0.42f, // 0.0 to 1.0
    val smoothness: Float = 0.18f,
    val spillReduction: Float = 0.35f,
    val backgroundMode: ChromaBackgroundMode = ChromaBackgroundMode.VIRTUAL_STUDIO
)

enum class LogoPosition(val label: String) {
    TOP_LEFT("Top Left"),
    TOP_RIGHT("Top Right"),
    BOTTOM_LEFT("Bottom Left"),
    BOTTOM_RIGHT("Bottom Right")
}

data class CustomLogoConfig(
    val isEnabled: Boolean = true,
    val position: LogoPosition = LogoPosition.TOP_RIGHT,
    val sizePercent: Int = 20, // 10% to 50%
    val opacity: Float = 0.90f,
    val watermarkText: String = "LIVE BROADCAST",
    val showTextLabel: Boolean = true,
    val customImageUri: String? = null,
    val customImageName: String? = null,
    val customImageMimeType: String? = null
)

enum class CustomMediaType(val label: String) {
    VIDEO("Video File"),
    IMAGE("Image File")
}

data class CustomMediaCastConfig(
    val uri: String? = null,
    val fileName: String = "PRESENTATION_REEL_FINAL_4K.MP4",
    val mediaType: CustomMediaType = CustomMediaType.VIDEO,
    val mimeType: String? = "video/mp4",
    val isPlaying: Boolean = true,
    val isLooping: Boolean = true
)

enum class AlertType(val title: String, val badgeText: String) {
    FOLLOWER("New Follower", "FOLLOW!"),
    SUBSCRIBER("New Subscriber", "TIER 1 SUB!"),
    SUPER_CHAT("Super Chat Donation", "DONATION!"),
    RAID("Incoming Channel Raid", "RAID ALERT!"),
    GIFT_SUB("Community Gift Subs", "GIFT x5!")
}

data class EngagementAlert(
    val id: String,
    val type: AlertType,
    val viewerName: String,
    val amountOrDetails: String,
    val customMessage: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class AudioTrack(
    val id: String,
    val name: String,
    val volume: Float = 1.0f, // 0.0 to 1.5
    val isMuted: Boolean = false,
    val peakDb: Float = -12f, // -60dB to 0dB
    val isNoiseGateActive: Boolean = true
)

enum class TransitionType(val label: String) {
    CUT("Cut (Instant)"),
    FADE("Fade (Crossfade)"),
    SLIDE("Slide Left")
}

data class StudioTelemetry(
    val fps: Int = 60,
    val bitrateKbps: Int = 6040,
    val droppedFramesPercent: Float = 0.02f,
    val cpuUsagePercent: Int = 18,
    val batteryPercent: Int = 94,
    val rtmpsLatencyMs: Int = 42,
    val hardwareEncoderEngaged: Boolean = true
)
