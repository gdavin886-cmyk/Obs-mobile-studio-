package com.example.util

import android.content.Context
import android.content.SharedPreferences
import com.example.model.*

class StudioPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("obs_studio_mobile_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_RETAIN_MEMORY = "retain_memory"
        private const val KEY_SELECTED_SCENE = "selected_scene"
        private const val KEY_WEBCAST_URL = "webcast_url"
        private const val KEY_BG_LIVE_ENABLED = "bg_live_enabled"
        private const val KEY_NOTIFICATION_POPUP = "notification_popup"
        private const val KEY_KEEP_AWAKE = "keep_awake"
        private const val KEY_AUTO_RECONNECT = "auto_reconnect"
        private const val KEY_WEB_TOUCH_INTERACTIVE = "web_touch_interactive"
        private const val KEY_AUDIO_SAMPLE_RATE = "audio_sample_rate"
        private const val KEY_AUDIO_BITRATE = "audio_bitrate"

        // Encoder
        private const val KEY_ENC_CODEC = "enc_codec"
        private const val KEY_ENC_RES = "enc_resolution"
        private const val KEY_ENC_BITRATE = "enc_bitrate"
        private const val KEY_ENC_FPS = "enc_fps"
        private const val KEY_ENC_LOW_LATENCY = "enc_low_latency"

        // Destinations key prefixes
        private const val PREFIX_DEST_ENABLED = "dest_enabled_"
        private const val PREFIX_DEST_KEY = "dest_key_"
        private const val PREFIX_DEST_URL = "dest_url_"
    }

    fun isRetainMemoryEnabled(): Boolean = prefs.getBoolean(KEY_RETAIN_MEMORY, true)

    fun setRetainMemoryEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_RETAIN_MEMORY, enabled).apply()
    }

    fun saveSettings(settings: StudioSettings) {
        prefs.edit()
            .putBoolean(KEY_RETAIN_MEMORY, settings.retainMemoryOnOpen)
            .putBoolean(KEY_BG_LIVE_ENABLED, settings.backgroundLiveEnabled)
            .putBoolean(KEY_NOTIFICATION_POPUP, settings.notificationPopupLive)
            .putBoolean(KEY_KEEP_AWAKE, settings.keepScreenAwake)
            .putBoolean(KEY_AUTO_RECONNECT, settings.autoReconnect)
            .putBoolean(KEY_WEB_TOUCH_INTERACTIVE, settings.webTouchInteractive)
            .putInt(KEY_AUDIO_SAMPLE_RATE, settings.audioSampleRateHz)
            .putInt(KEY_AUDIO_BITRATE, settings.audioBitrateKbps)
            .apply()
    }

    fun loadSettings(): StudioSettings {
        return StudioSettings(
            backgroundLiveEnabled = prefs.getBoolean(KEY_BG_LIVE_ENABLED, true),
            notificationPopupLive = prefs.getBoolean(KEY_NOTIFICATION_POPUP, true),
            keepScreenAwake = prefs.getBoolean(KEY_KEEP_AWAKE, true),
            autoReconnect = prefs.getBoolean(KEY_AUTO_RECONNECT, true),
            retainMemoryOnOpen = prefs.getBoolean(KEY_RETAIN_MEMORY, true),
            webTouchInteractive = prefs.getBoolean(KEY_WEB_TOUCH_INTERACTIVE, true),
            audioSampleRateHz = prefs.getInt(KEY_AUDIO_SAMPLE_RATE, 48000),
            audioBitrateKbps = prefs.getInt(KEY_AUDIO_BITRATE, 160)
        )
    }

    fun saveCurrentScene(sceneId: SceneId) {
        if (!isRetainMemoryEnabled()) return
        prefs.edit().putString(KEY_SELECTED_SCENE, sceneId.name).apply()
    }

    fun loadSavedScene(): SceneId? {
        if (!isRetainMemoryEnabled()) return null
        val name = prefs.getString(KEY_SELECTED_SCENE, null) ?: return null
        return try {
            SceneId.valueOf(name)
        } catch (_: Exception) {
            null
        }
    }

    fun saveWebCastUrl(url: String) {
        if (!isRetainMemoryEnabled()) return
        prefs.edit().putString(KEY_WEBCAST_URL, url).apply()
    }

    fun loadSavedWebCastUrl(): String? {
        if (!isRetainMemoryEnabled()) return null
        return prefs.getString(KEY_WEBCAST_URL, null)
    }

    fun saveEncoderConfig(config: EncoderConfig) {
        if (!isRetainMemoryEnabled()) return
        prefs.edit()
            .putString(KEY_ENC_CODEC, config.codec.name)
            .putString(KEY_ENC_RES, config.resolution.name)
            .putInt(KEY_ENC_BITRATE, config.targetBitrateKbps)
            .putInt(KEY_ENC_FPS, config.fps)
            .putBoolean(KEY_ENC_LOW_LATENCY, config.isLowLatencyMode)
            .apply()
    }

    fun loadEncoderConfig(): EncoderConfig? {
        if (!isRetainMemoryEnabled()) return null
        val codecName = prefs.getString(KEY_ENC_CODEC, null) ?: return null
        val resName = prefs.getString(KEY_ENC_RES, null) ?: return null
        return try {
            EncoderConfig(
                codec = HardwareCodec.valueOf(codecName),
                resolution = StreamResolution.valueOf(resName),
                targetBitrateKbps = prefs.getInt(KEY_ENC_BITRATE, 6000),
                fps = prefs.getInt(KEY_ENC_FPS, 60),
                isLowLatencyMode = prefs.getBoolean(KEY_ENC_LOW_LATENCY, true)
            )
        } catch (_: Exception) {
            null
        }
    }

    fun saveDestination(dest: StreamDestination) {
        if (!isRetainMemoryEnabled()) return
        prefs.edit()
            .putBoolean(PREFIX_DEST_ENABLED + dest.id, dest.isEnabled)
            .putString(PREFIX_DEST_KEY + dest.id, dest.streamKey)
            .putString(PREFIX_DEST_URL + dest.id, dest.serverUrl)
            .apply()
    }

    fun restoreDestination(dest: StreamDestination): StreamDestination {
        if (!isRetainMemoryEnabled()) return dest
        val isEnabled = prefs.getBoolean(PREFIX_DEST_ENABLED + dest.id, dest.isEnabled)
        val key = prefs.getString(PREFIX_DEST_KEY + dest.id, dest.streamKey) ?: dest.streamKey
        val url = prefs.getString(PREFIX_DEST_URL + dest.id, dest.serverUrl) ?: dest.serverUrl
        return dest.copy(isEnabled = isEnabled, streamKey = key, serverUrl = url)
    }

    fun saveCustomLogo(logo: CustomLogoConfig) {
        if (!isRetainMemoryEnabled()) return
        prefs.edit()
            .putBoolean("logo_enabled", logo.isEnabled)
            .putString("logo_pos", logo.position.name)
            .putInt("logo_size", logo.sizePercent)
            .putFloat("logo_opacity", logo.opacity)
            .putString("logo_text", logo.watermarkText)
            .putString("logo_text_pos", logo.textPosition.name)
            .putBoolean("logo_show_frame", logo.showLogoFrame)
            .putBoolean("logo_show_bg", logo.showBackground)
            .putString("logo_bg_mode", logo.backgroundColorMode.name)
            .putString("logo_custom_bg", logo.customBgColorHex)
            .putBoolean("logo_cd_enabled", logo.isCountdownEnabled)
            .putInt("logo_cd_total", logo.countdownTotalSeconds)
            .putString("logo_cd_pos", logo.countdownPosition.name)
            .putString("logo_cd_next_text", logo.countdownNextText)
            .putString("logo_uri", logo.customImageUri)
            .putString("logo_name", logo.customImageName)
            .apply()
    }

    fun loadCustomLogo(): CustomLogoConfig {
        if (!isRetainMemoryEnabled()) return CustomLogoConfig()
        return try {
            CustomLogoConfig(
                isEnabled = prefs.getBoolean("logo_enabled", true),
                position = LogoPosition.valueOf(prefs.getString("logo_pos", LogoPosition.TOP_RIGHT.name) ?: LogoPosition.TOP_RIGHT.name),
                sizePercent = prefs.getInt("logo_size", 20),
                opacity = prefs.getFloat("logo_opacity", 0.90f),
                watermarkText = prefs.getString("logo_text", "LIVE BROADCAST") ?: "LIVE BROADCAST",
                textPosition = TextRelativePosition.valueOf(prefs.getString("logo_text_pos", TextRelativePosition.RIGHT.name) ?: TextRelativePosition.RIGHT.name),
                showLogoFrame = prefs.getBoolean("logo_show_frame", false),
                showBackground = prefs.getBoolean("logo_show_bg", true),
                backgroundColorMode = WatermarkBgColorMode.valueOf(prefs.getString("logo_bg_mode", WatermarkBgColorMode.DARK_GLASS.name) ?: WatermarkBgColorMode.DARK_GLASS.name),
                customBgColorHex = prefs.getString("logo_custom_bg", "#1E293B") ?: "#1E293B",
                isCountdownEnabled = prefs.getBoolean("logo_cd_enabled", false),
                countdownTotalSeconds = prefs.getInt("logo_cd_total", 60),
                countdownRemainingSeconds = prefs.getInt("logo_cd_total", 60),
                countdownPosition = CountdownPosition.valueOf(prefs.getString("logo_cd_pos", CountdownPosition.UNDER.name) ?: CountdownPosition.UNDER.name),
                countdownNextText = prefs.getString("logo_cd_next_text", "STREAM ON AIR") ?: "STREAM ON AIR",
                customImageUri = prefs.getString("logo_uri", null),
                customImageName = prefs.getString("logo_name", null)
            )
        } catch (_: Exception) {
            CustomLogoConfig()
        }
    }

    fun saveScrollingText(ticker: ScrollingTextConfig) {
        if (!isRetainMemoryEnabled()) return
        prefs.edit()
            .putBoolean("ticker_enabled", ticker.isEnabled)
            .putBoolean("ticker_perm", ticker.isPermanentRunning)
            .putString("ticker_text", ticker.textContent)
            .putString("ticker_mod", ticker.mod.name)
            .putString("ticker_speed", ticker.speed.name)
            .putBoolean("ticker_prefix", ticker.showPrefixBadge)
            .apply()
    }

    fun loadScrollingText(): ScrollingTextConfig {
        if (!isRetainMemoryEnabled()) return ScrollingTextConfig()
        return try {
            ScrollingTextConfig(
                isEnabled = prefs.getBoolean("ticker_enabled", true),
                isPermanentRunning = prefs.getBoolean("ticker_perm", false),
                textContent = prefs.getString("ticker_text", "WELCOME TO THE STREAM! Multi-destination broadcasting active across Twitch, YouTube, OK.ru and Telegram.") ?: "WELCOME TO THE STREAM!",
                mod = MarqueeMod.valueOf(prefs.getString("ticker_mod", MarqueeMod.BROADCAST_INFORM.name) ?: MarqueeMod.BROADCAST_INFORM.name),
                speed = MarqueeSpeed.valueOf(prefs.getString("ticker_speed", MarqueeSpeed.NORMAL.name) ?: MarqueeSpeed.NORMAL.name),
                showPrefixBadge = prefs.getBoolean("ticker_prefix", true)
            )
        } catch (_: Exception) {
            ScrollingTextConfig()
        }
    }

    fun clearAllMemory() {
        prefs.edit().clear().apply()
    }
}
