package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class StudioViewModel : ViewModel() {

    // Broadcasting & Recording State
    private val _isLive = MutableStateFlow(false)
    val isLive: StateFlow<Boolean> = _isLive.asStateFlow()

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _isStudioMode = MutableStateFlow(false)
    val isStudioMode: StateFlow<Boolean> = _isStudioMode.asStateFlow()

    private val _streamDurationSeconds = MutableStateFlow(0L)
    val streamDurationSeconds: StateFlow<Long> = _streamDurationSeconds.asStateFlow()

    // Scenes & Transition
    private val defaultScenes = listOf(
        StudioScene(
            id = SceneId.CAMERA_GREENSCREEN,
            name = "Camera + Chroma Key",
            sources = listOf(
                SourceItem("cam_main", SourceType.CAMERA, "Main Camera (Chroma Key)", isVisible = true),
                SourceItem("logo_ovl", SourceType.CUSTOM_LOGO, "Custom Logo Watermark", isVisible = true),
                SourceItem("alert_box", SourceType.ALERT_OVERLAY, "Engagement Alert Box", isVisible = true)
            )
        ),
        StudioScene(
            id = SceneId.SCREEN_CAST,
            name = "Screen Share + PIP",
            sources = listOf(
                SourceItem("screen_cap", SourceType.SCREEN, "Mobile Display Capture", isVisible = true),
                SourceItem("pip_facecam", SourceType.CAMERA, "PIP Facecam Overlay", isVisible = true),
                SourceItem("logo_ovl", SourceType.CUSTOM_LOGO, "Custom Logo Watermark", isVisible = true),
                SourceItem("alert_box", SourceType.ALERT_OVERLAY, "Engagement Alert Box", isVisible = true)
            )
        ),
        StudioScene(
            id = SceneId.WEB_CAST,
            name = "Website Page Cast",
            sources = listOf(
                SourceItem("web_view", SourceType.WEB_PAGE, "Interactive Web Browser", isVisible = true),
                SourceItem("logo_ovl", SourceType.CUSTOM_LOGO, "Custom Logo Watermark", isVisible = true),
                SourceItem("alert_box", SourceType.ALERT_OVERLAY, "Engagement Alert Box", isVisible = true)
            )
        ),
        StudioScene(
            id = SceneId.MEDIA_CAST,
            name = "Media File Video Cast",
            sources = listOf(
                SourceItem("file_video", SourceType.FILE_MEDIA, "Broadcast Video / Slides", isVisible = true),
                SourceItem("logo_ovl", SourceType.CUSTOM_LOGO, "Custom Logo Watermark", isVisible = true),
                SourceItem("alert_box", SourceType.ALERT_OVERLAY, "Engagement Alert Box", isVisible = true)
            )
        ),
        StudioScene(
            id = SceneId.STARTING_SOON,
            name = "Starting Soon / BRB",
            sources = listOf(
                SourceItem("intro_anim", SourceType.FILE_MEDIA, "Countdown Timer & Intro", isVisible = true),
                SourceItem("logo_ovl", SourceType.CUSTOM_LOGO, "Custom Logo Watermark", isVisible = true),
                SourceItem("alert_box", SourceType.ALERT_OVERLAY, "Engagement Alert Box", isVisible = true)
            )
        )
    )

    private val _scenes = MutableStateFlow(defaultScenes)
    val scenes: StateFlow<List<StudioScene>> = _scenes.asStateFlow()

    private val _programScene = MutableStateFlow(defaultScenes[0])
    val programScene: StateFlow<StudioScene> = _programScene.asStateFlow()

    private val _previewScene = MutableStateFlow(defaultScenes[1])
    val previewScene: StateFlow<StudioScene> = _previewScene.asStateFlow()

    private val _transitionType = MutableStateFlow(TransitionType.FADE)
    val transitionType: StateFlow<TransitionType> = _transitionType.asStateFlow()

    private val _transitionDurationMs = MutableStateFlow(300)
    val transitionDurationMs: StateFlow<Int> = _transitionDurationMs.asStateFlow()

    // Multi-Destination Streaming
    private val defaultDestinations = listOf(
        StreamDestination(
            id = "dest_twitch",
            platform = DestinationPlatform.TWITCH,
            serverUrl = "rtmps://live.twitch.tv/app/",
            streamKey = "live_928174_uJ8aBqZkXm91Kpl",
            isEnabled = true
        ),
        StreamDestination(
            id = "dest_youtube",
            platform = DestinationPlatform.YOUTUBE,
            serverUrl = "rtmps://a.rtmp.youtube.com/live2",
            streamKey = "yt_live_kx89_4721_mqqb",
            isEnabled = true
        ),
        StreamDestination(
            id = "dest_facebook",
            platform = DestinationPlatform.FACEBOOK,
            serverUrl = "rtmps://live-api-s.facebook.com:443/rtmp/",
            streamKey = "FB-847291038472-0-LIVE",
            isEnabled = false
        ),
        StreamDestination(
            id = "dest_custom",
            platform = DestinationPlatform.CUSTOM_RTMPS,
            serverUrl = "rtmps://stream.obs-mobile.studio:443/live/",
            streamKey = "obs_studio_custom_token",
            isEnabled = false
        )
    )

    private val _destinations = MutableStateFlow(defaultDestinations)
    val destinations: StateFlow<List<StreamDestination>> = _destinations.asStateFlow()

    // Encoder Configuration
    private val _encoderConfig = MutableStateFlow(EncoderConfig())
    val encoderConfig: StateFlow<EncoderConfig> = _encoderConfig.asStateFlow()

    // Chroma Key Filter Configuration
    private val _chromaKeyConfig = MutableStateFlow(ChromaKeyConfig())
    val chromaKeyConfig: StateFlow<ChromaKeyConfig> = _chromaKeyConfig.asStateFlow()

    // Custom Logo Configuration
    private val _customLogoConfig = MutableStateFlow(CustomLogoConfig())
    val customLogoConfig: StateFlow<CustomLogoConfig> = _customLogoConfig.asStateFlow()

    // Telemetry & Hardware Stats
    private val _telemetry = MutableStateFlow(StudioTelemetry())
    val telemetry: StateFlow<StudioTelemetry> = _telemetry.asStateFlow()

    // Audio Mixer
    private val _audioTracks = MutableStateFlow(
        listOf(
            AudioTrack("mic_aux", "Mic / Audio In", volume = 1.0f, isMuted = false, peakDb = -14f),
            AudioTrack("desktop_sys", "Desktop / Screen Audio", volume = 0.85f, isMuted = false, peakDb = -18f),
            AudioTrack("media_player", "Media / Video Source", volume = 0.70f, isMuted = false, peakDb = -22f),
            AudioTrack("alert_sfx", "Viewer Alert SFX", volume = 0.90f, isMuted = false, peakDb = -10f)
        )
    )
    val audioTracks: StateFlow<List<AudioTrack>> = _audioTracks.asStateFlow()

    // Engagement Alerts Queue
    private val _activeAlert = MutableStateFlow<EngagementAlert?>(null)
    val activeAlert: StateFlow<EngagementAlert?> = _activeAlert.asStateFlow()

    // Camera & Web Cast States
    private val _isFrontCamera = MutableStateFlow(false)
    val isFrontCamera: StateFlow<Boolean> = _isFrontCamera.asStateFlow()

    private val _isTorchOn = MutableStateFlow(false)
    val isTorchOn: StateFlow<Boolean> = _isTorchOn.asStateFlow()

    private val _webCastUrl = MutableStateFlow("https://en.wikipedia.org/wiki/Open_Broadcaster_Software")
    val webCastUrl: StateFlow<String> = _webCastUrl.asStateFlow()

    private val _mediaIsPlaying = MutableStateFlow(true)
    val mediaIsPlaying: StateFlow<Boolean> = _mediaIsPlaying.asStateFlow()

    private val _mediaCastConfig = MutableStateFlow(CustomMediaCastConfig())
    val mediaCastConfig: StateFlow<CustomMediaCastConfig> = _mediaCastConfig.asStateFlow()

    private val _screenCastGameMode = MutableStateFlow(true)
    val screenCastGameMode: StateFlow<Boolean> = _screenCastGameMode.asStateFlow()

    private var tickerJob: Job? = null
    private var alertDismissJob: Job? = null

    init {
        startTelemetrySimulation()
    }

    private fun startTelemetrySimulation() {
        viewModelScope.launch {
            while (isActive) {
                delay(1200)
                if (_isLive.value) {
                    val baseBitrate = _encoderConfig.value.targetBitrateKbps
                    val jitterBitrate = (baseBitrate + Random.nextInt(-180, 180)).coerceAtLeast(500)
                    val latency = if (_encoderConfig.value.isLowLatencyMode) Random.nextInt(32, 58) else Random.nextInt(120, 240)
                    _telemetry.value = _telemetry.value.copy(
                        fps = if (Random.nextFloat() > 0.95f) 59 else 60,
                        bitrateKbps = jitterBitrate,
                        droppedFramesPercent = (Random.nextFloat() * 0.05f).coerceAtLeast(0f),
                        cpuUsagePercent = Random.nextInt(16, 24),
                        rtmpsLatencyMs = latency,
                        hardwareEncoderEngaged = _encoderConfig.value.codec.isHardware
                    )

                    // Update live destination readouts
                    _destinations.update { list ->
                        list.map { dest ->
                            if (dest.isEnabled) {
                                dest.copy(
                                    isLive = true,
                                    currentBitrateKbps = jitterBitrate + Random.nextInt(-40, 40),
                                    latencyMs = latency + Random.nextInt(-5, 5),
                                    droppedFramesPercent = 0.01f
                                )
                            } else {
                                dest.copy(isLive = false, currentBitrateKbps = 0, latencyMs = 0)
                            }
                        }
                    }
                } else {
                    _telemetry.value = _telemetry.value.copy(
                        bitrateKbps = 0,
                        cpuUsagePercent = Random.nextInt(7, 12),
                        rtmpsLatencyMs = 0
                    )
                }

                // Simulate slight audio meter fluctuation
                _audioTracks.update { list ->
                    list.map { track ->
                        if (track.isMuted) {
                            track.copy(peakDb = -60f)
                        } else {
                            val base = if (track.id == "mic_aux") -14f else -20f
                            val jitter = (Random.nextFloat() * 8f) - 4f
                            track.copy(peakDb = (base + jitter).coerceIn(-60f, -2f))
                        }
                    }
                }
            }
        }
    }

    fun toggleLive() {
        val nextLive = !_isLive.value
        _isLive.value = nextLive

        if (nextLive) {
            _streamDurationSeconds.value = 0L
            tickerJob?.cancel()
            tickerJob = viewModelScope.launch {
                while (isActive) {
                    delay(1000)
                    _streamDurationSeconds.update { it + 1 }
                }
            }
            _destinations.update { list ->
                list.map { if (it.isEnabled) it.copy(isLive = true) else it }
            }
        } else {
            tickerJob?.cancel()
            _streamDurationSeconds.value = 0L
            _destinations.update { list ->
                list.map { it.copy(isLive = false, currentBitrateKbps = 0, latencyMs = 0) }
            }
        }
    }

    fun toggleRecording() {
        _isRecording.update { !it }
    }

    fun toggleStudioMode() {
        _isStudioMode.update { !it }
    }

    fun selectScene(scene: StudioScene) {
        if (_isStudioMode.value) {
            _previewScene.value = scene
        } else {
            _programScene.value = scene
        }
    }

    fun transitionStudio() {
        val staged = _previewScene.value
        val current = _programScene.value
        _programScene.value = staged
        _previewScene.value = current
    }

    fun setTransitionType(type: TransitionType) {
        _transitionType.value = type
    }

    fun setTransitionDuration(ms: Int) {
        _transitionDurationMs.value = ms
    }

    fun toggleSourceVisibility(sourceId: String) {
        val currentSceneId = _programScene.value.id
        _scenes.update { allScenes ->
            allScenes.map { scene ->
                if (scene.id == currentSceneId) {
                    val updatedSources = scene.sources.map { src ->
                        if (src.id == sourceId) src.copy(isVisible = !src.isVisible) else src
                    }
                    scene.copy(sources = updatedSources)
                } else scene
            }
        }
        _programScene.update { scene ->
            val updatedSources = scene.sources.map { src ->
                if (src.id == sourceId) src.copy(isVisible = !src.isVisible) else src
            }
            scene.copy(sources = updatedSources)
        }
    }

    fun toggleSourceLock(sourceId: String) {
        val currentSceneId = _programScene.value.id
        _scenes.update { allScenes ->
            allScenes.map { scene ->
                if (scene.id == currentSceneId) {
                    val updatedSources = scene.sources.map { src ->
                        if (src.id == sourceId) src.copy(isLocked = !src.isLocked) else src
                    }
                    scene.copy(sources = updatedSources)
                } else scene
            }
        }
        _programScene.update { scene ->
            val updatedSources = scene.sources.map { src ->
                if (src.id == sourceId) src.copy(isLocked = !src.isLocked) else src
            }
            scene.copy(sources = updatedSources)
        }
    }

    // Engagement Alert Overlay Trigger
    fun triggerEngagementAlert(
        type: AlertType,
        viewerName: String,
        amountOrDetails: String,
        customMessage: String
    ) {
        val alert = EngagementAlert(
            id = "alert_${System.currentTimeMillis()}",
            type = type,
            viewerName = viewerName,
            amountOrDetails = amountOrDetails,
            customMessage = customMessage
        )
        _activeAlert.value = alert

        alertDismissJob?.cancel()
        alertDismissJob = viewModelScope.launch {
            delay(4500)
            _activeAlert.value = null
        }
    }

    fun dismissAlert() {
        alertDismissJob?.cancel()
        _activeAlert.value = null
    }

    // Destinations Management
    fun toggleDestination(destId: String) {
        _destinations.update { list ->
            list.map { if (it.id == destId) it.copy(isEnabled = !it.isEnabled) else it }
        }
    }

    fun updateDestination(updated: StreamDestination) {
        _destinations.update { list ->
            list.map { if (it.id == updated.id) updated else it }
        }
    }

    // Chroma Key
    fun updateChromaKey(config: ChromaKeyConfig) {
        _chromaKeyConfig.value = config
    }

    // Custom Logo
    fun updateCustomLogo(config: CustomLogoConfig) {
        _customLogoConfig.value = config
    }

    // Encoder Configuration
    fun updateEncoderConfig(config: EncoderConfig) {
        _encoderConfig.value = config
        _telemetry.update {
            it.copy(
                bitrateKbps = if (_isLive.value) config.targetBitrateKbps else 0,
                hardwareEncoderEngaged = config.codec.isHardware
            )
        }
    }

    // Audio Mixer
    fun setAudioVolume(trackId: String, volume: Float) {
        _audioTracks.update { list ->
            list.map { if (it.id == trackId) it.copy(volume = volume) else it }
        }
    }

    fun toggleAudioMute(trackId: String) {
        _audioTracks.update { list ->
            list.map { if (it.id == trackId) it.copy(isMuted = !it.isMuted) else it }
        }
    }

    fun toggleNoiseGate(trackId: String) {
        _audioTracks.update { list ->
            list.map { if (it.id == trackId) it.copy(isNoiseGateActive = !it.isNoiseGateActive) else it }
        }
    }

    // Camera & Media Toggles
    fun switchCamera() {
        _isFrontCamera.update { !it }
    }

    fun toggleTorch() {
        _isTorchOn.update { !it }
    }

    fun setWebCastUrl(url: String) {
        val sanitized = if (!url.startsWith("http://") && !url.startsWith("https://")) {
            "https://$url"
        } else {
            url
        }
        _webCastUrl.value = sanitized
    }

    fun toggleMediaPlayback() {
        _mediaIsPlaying.update { !it }
        _mediaCastConfig.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun setMediaCastSource(uri: String?, fileName: String, mediaType: CustomMediaType, mimeType: String?) {
        _mediaCastConfig.value = CustomMediaCastConfig(
            uri = uri,
            fileName = fileName,
            mediaType = mediaType,
            mimeType = mimeType,
            isPlaying = true
        )
        _mediaIsPlaying.value = true
    }

    fun clearMediaCastSource() {
        _mediaCastConfig.value = CustomMediaCastConfig()
        _mediaIsPlaying.value = true
    }

    fun setCustomLogoFile(uri: String?, fileName: String?, mimeType: String?) {
        _customLogoConfig.update {
            it.copy(
                customImageUri = uri,
                customImageName = fileName,
                customImageMimeType = mimeType,
                isEnabled = true
            )
        }
    }

    fun clearCustomLogoFile() {
        _customLogoConfig.update {
            it.copy(
                customImageUri = null,
                customImageName = null,
                customImageMimeType = null
            )
        }
    }

    fun toggleScreenCastGameMode() {
        _screenCastGameMode.update { !it }
    }
}
