package com.example.telegram

/**
 * Status indicator for Telegram Channel Live:
 * TELEGRAM: CONNECTING
 * TELEGRAM: CONNECTED
 * TELEGRAM: LIVE
 * TELEGRAM: ERROR
 */
enum class TelegramLiveStatus(val label: String) {
    IDLE("TELEGRAM: IDLE"),
    CONNECTING("TELEGRAM: CONNECTING"),
    CONNECTED("TELEGRAM: CONNECTED"),
    LIVE("TELEGRAM: LIVE"),
    ERROR("TELEGRAM: ERROR")
}

data class TelegramChannel(
    val id: Long,
    val accessHash: Long,
    val title: String,
    val username: String? = null,
    val isCreator: Boolean = false,
    val hasAdminRights: Boolean = true,
    val canManageCall: Boolean = true,
    val activeGroupCallId: Long? = null,
    val activeGroupCallAccessHash: Long? = null
)

data class TelegramAuthState(
    val isAuthenticated: Boolean = false,
    val userId: Long? = null,
    val userName: String? = null,
    val phoneNumber: String? = null,
    val phoneCodeHash: String? = null,
    val apiId: Int = 0,
    val apiHash: String = "",
    val error: String? = null
)

/**
 * Encapsulates the secret stream key.
 * Overrides toString() to guarantee the secret key is NEVER printed in Logcat or logging statements.
 */
class ProtectedStreamKey(private val rawKey: String) {
    fun getSecret(): String = rawKey

    override fun toString(): String = "[PROTECTED_TELEGRAM_STREAM_KEY]"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProtectedStreamKey) return false
        return rawKey == other.rawKey
    }

    override fun hashCode(): Int = rawKey.hashCode()
}

data class TelegramRtmpCredentials(
    val rtmpUrl: String,
    val streamKey: ProtectedStreamKey
)

data class TelegramLivestreamSession(
    val channel: TelegramChannel? = null,
    val activeGroupCall: TLInputGroupCall? = null,
    val credentials: TelegramRtmpCredentials? = null,
    val status: TelegramLiveStatus = TelegramLiveStatus.IDLE,
    val statusMessage: String? = null,
    val isCreatedByApp: Boolean = false,
    val autoDiscardOnStop: Boolean = true
)
