package com.example.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R

class StudioBroadcastService : Service() {

    companion object {
        const val CHANNEL_ID_HIGH = "obs_studio_live_high"
        const val CHANNEL_ID_DEFAULT = "obs_studio_live_default"
        const val NOTIFICATION_ID = 1001

        const val ACTION_START_BROADCAST = "com.example.service.ACTION_START_BROADCAST"
        const val ACTION_STOP_BROADCAST = "com.example.service.ACTION_STOP_BROADCAST"
        const val ACTION_UPDATE_STATS = "com.example.service.ACTION_UPDATE_STATS"
        const val ACTION_STOP_FROM_NOTIFICATION = "com.example.service.ACTION_STOP_FROM_NOTIFICATION"
        const val ACTION_TOGGLE_MUTE_FROM_NOTIFICATION = "com.example.service.ACTION_TOGGLE_MUTE"

        const val EXTRA_IS_LIVE = "extra_is_live"
        const val EXTRA_DURATION = "extra_duration"
        const val EXTRA_BITRATE = "extra_bitrate"
        const val EXTRA_DESTINATIONS = "extra_destinations"
        const val EXTRA_POPUP_ENABLED = "extra_popup_enabled"
        const val EXTRA_IS_MUTED = "extra_is_muted"

        // Callback for ViewModels to observe actions from notifications
        var onNotificationActionTriggered: ((String) -> Unit)? = null

        fun startService(
            context: Context,
            isLive: Boolean,
            duration: String,
            bitrateKbps: Int,
            destinations: String,
            isPopupLive: Boolean,
            isMuted: Boolean = false
        ) {
            val intent = Intent(context, StudioBroadcastService::class.java).apply {
                action = ACTION_START_BROADCAST
                putExtra(EXTRA_IS_LIVE, isLive)
                putExtra(EXTRA_DURATION, duration)
                putExtra(EXTRA_BITRATE, bitrateKbps)
                putExtra(EXTRA_DESTINATIONS, destinations)
                putExtra(EXTRA_POPUP_ENABLED, isPopupLive)
                putExtra(EXTRA_IS_MUTED, isMuted)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun updateService(
            context: Context,
            duration: String,
            bitrateKbps: Int,
            destinations: String,
            isPopupLive: Boolean,
            isMuted: Boolean = false
        ) {
            val intent = Intent(context, StudioBroadcastService::class.java).apply {
                action = ACTION_UPDATE_STATS
                putExtra(EXTRA_DURATION, duration)
                putExtra(EXTRA_BITRATE, bitrateKbps)
                putExtra(EXTRA_DESTINATIONS, destinations)
                putExtra(EXTRA_POPUP_ENABLED, isPopupLive)
                putExtra(EXTRA_IS_MUTED, isMuted)
            }
            context.startService(intent)
        }

        fun stopService(context: Context) {
            val intent = Intent(context, StudioBroadcastService::class.java).apply {
                action = ACTION_STOP_BROADCAST
            }
            context.startService(intent)
        }
    }

    private var isPopupLive = true
    private var isMuted = false
    private var lastDuration = "00:00:00"
    private var lastBitrate = 6000
    private var lastDestinations = "Twitch, OK.ru, Telegram"

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: return START_NOT_STICKY

        when (action) {
            ACTION_START_BROADCAST -> {
                isPopupLive = intent.getBooleanExtra(EXTRA_POPUP_ENABLED, true)
                isMuted = intent.getBooleanExtra(EXTRA_IS_MUTED, false)
                lastDuration = intent.getStringExtra(EXTRA_DURATION) ?: "00:00:00"
                lastBitrate = intent.getIntExtra(EXTRA_BITRATE, 6000)
                lastDestinations = intent.getStringExtra(EXTRA_DESTINATIONS) ?: "Active Platforms"

                val notification = buildNotification(lastDuration, lastBitrate, lastDestinations, isMuted, isPopupLive)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    try {
                        startForeground(
                            NOTIFICATION_ID,
                            notification,
                            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                        )
                    } catch (_: Exception) {
                        startForeground(NOTIFICATION_ID, notification)
                    }
                } else {
                    startForeground(NOTIFICATION_ID, notification)
                }
            }

            ACTION_UPDATE_STATS -> {
                isPopupLive = intent.getBooleanExtra(EXTRA_POPUP_ENABLED, isPopupLive)
                isMuted = intent.getBooleanExtra(EXTRA_IS_MUTED, isMuted)
                lastDuration = intent.getStringExtra(EXTRA_DURATION) ?: lastDuration
                lastBitrate = intent.getIntExtra(EXTRA_BITRATE, lastBitrate)
                lastDestinations = intent.getStringExtra(EXTRA_DESTINATIONS) ?: lastDestinations

                val notification = buildNotification(lastDuration, lastBitrate, lastDestinations, isMuted, isPopupLive)
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(NOTIFICATION_ID, notification)
            }

            ACTION_STOP_FROM_NOTIFICATION -> {
                onNotificationActionTriggered?.invoke(ACTION_STOP_FROM_NOTIFICATION)
                stopForeground(true)
                stopSelf()
            }

            ACTION_TOGGLE_MUTE_FROM_NOTIFICATION -> {
                isMuted = !isMuted
                onNotificationActionTriggered?.invoke(ACTION_TOGGLE_MUTE_FROM_NOTIFICATION)
                val notification = buildNotification(lastDuration, lastBitrate, lastDestinations, isMuted, isPopupLive)
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(NOTIFICATION_ID, notification)
            }

            ACTION_STOP_BROADCAST -> {
                stopForeground(true)
                stopSelf()
            }
        }

        return START_STICKY
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Heads-up pop-up channel (High importance)
            val channelHigh = NotificationChannel(
                CHANNEL_ID_HIGH,
                "OBS Studio Live (Pop-up Alerts)",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Shows live broadcasting heads-up banner and quick controls"
                enableLights(true)
                enableVibration(false)
            }
            manager.createNotificationChannel(channelHigh)

            // Standard silent background channel (Low/Default importance)
            val channelDefault = NotificationChannel(
                CHANNEL_ID_DEFAULT,
                "OBS Studio Background Live",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Keeps live broadcast encoder active in background"
            }
            manager.createNotificationChannel(channelDefault)
        }
    }

    private fun buildNotification(
        duration: String,
        bitrate: Int,
        destinations: String,
        muted: Boolean,
        popup: Boolean
    ): Notification {
        val channelId = if (popup) CHANNEL_ID_HIGH else CHANNEL_ID_DEFAULT

        val contentIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val stopIntent = Intent(this, StudioBroadcastService::class.java).apply {
            action = ACTION_STOP_FROM_NOTIFICATION
        }
        val stopPendingIntent = PendingIntent.getService(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val muteIntent = Intent(this, StudioBroadcastService::class.java).apply {
            action = ACTION_TOGGLE_MUTE_FROM_NOTIFICATION
        }
        val mutePendingIntent = PendingIntent.getService(
            this,
            2,
            muteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val muteLabel = if (muted) "UNMUTE MIC" else "MUTE MIC"

        return NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("🔴 OBS Studio Live - BROADCASTING")
            .setContentText("Duration: $duration • $bitrate kbps • $destinations")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("🔴 LIVE BROADCAST IN PROGRESS\nTime: $duration | Bitrate: $bitrate kbps\nDestinations: $destinations\nMic: ${if (muted) "MUTED" else "ACTIVE"}")
            )
            .setContentIntent(contentPendingIntent)
            .setOngoing(true)
            .setPriority(if (popup) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_LOW)
            .addAction(android.R.drawable.ic_media_pause, "⏹️ STOP LIVE", stopPendingIntent)
            .addAction(android.R.drawable.ic_lock_silent_mode, "🎙️ $muteLabel", mutePendingIntent)
            .build()
    }
}
