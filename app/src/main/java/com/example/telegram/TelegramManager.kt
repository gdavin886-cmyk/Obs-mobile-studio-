package com.example.telegram

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Singleton manager coordinating the Telegram RTMP Livestream lifecycle.
 */
object TelegramManager {
    private val client = TelegramClient()

    private val _authState = MutableStateFlow(TelegramAuthState())
    val authState: StateFlow<TelegramAuthState> = _authState.asStateFlow()

    private val _adminChannels = MutableStateFlow<List<TelegramChannel>>(emptyList())
    val adminChannels: StateFlow<List<TelegramChannel>> = _adminChannels.asStateFlow()

    private val _selectedChannel = MutableStateFlow<TelegramChannel?>(null)
    val selectedChannel: StateFlow<TelegramChannel?> = _selectedChannel.asStateFlow()

    private val _session = MutableStateFlow(TelegramLivestreamSession())
    val session: StateFlow<TelegramLivestreamSession> = _session.asStateFlow()

    private val _liveStatus = MutableStateFlow(TelegramLiveStatus.IDLE)
    val liveStatus: StateFlow<TelegramLiveStatus> = _liveStatus.asStateFlow()

    fun updateLiveStatus(status: TelegramLiveStatus, message: String? = null) {
        _liveStatus.value = status
        _session.update { it.copy(status = status, statusMessage = message) }
    }

    suspend fun sendVerificationCode(phoneNumber: String, apiId: Int, apiHash: String): Result<String> {
        val result = client.sendCode(phoneNumber, apiId, apiHash)
        result.onSuccess { hash ->
            _authState.update {
                it.copy(
                    phoneNumber = phoneNumber,
                    phoneCodeHash = hash,
                    apiId = apiId,
                    apiHash = apiHash,
                    error = null
                )
            }
        }.onFailure { err ->
            _authState.update { it.copy(error = err.message) }
        }
        return result
    }

    suspend fun signInWithCode(code: String): Result<TelegramAuthState> {
        val current = _authState.value
        val phone = current.phoneNumber ?: return Result.failure(IllegalStateException("Phone number missing"))
        val hash = current.phoneCodeHash ?: return Result.failure(IllegalStateException("Phone code hash missing"))

        val result = client.signIn(phone, hash, code)
        result.onSuccess { state ->
            _authState.value = state
            refreshAdminChannels()
        }.onFailure { err ->
            _authState.update { it.copy(error = err.message) }
        }
        return result
    }

    suspend fun signInWithBotToken(botToken: String, apiId: Int, apiHash: String): Result<TelegramAuthState> {
        val result = client.importBotAuthorization(apiId, apiHash, botToken)
        result.onSuccess { state ->
            _authState.value = state
            refreshAdminChannels()
        }.onFailure { err ->
            _authState.update { it.copy(error = err.message) }
        }
        return result
    }

    fun logout() {
        _authState.value = TelegramAuthState()
        _adminChannels.value = emptyList()
        _selectedChannel.value = null
        _session.value = TelegramLivestreamSession()
        _liveStatus.value = TelegramLiveStatus.IDLE
    }

    suspend fun refreshAdminChannels(): Result<List<TelegramChannel>> {
        val result = client.getAdminChannels()
        result.onSuccess { channels ->
            _adminChannels.value = channels
            if (_selectedChannel.value == null && channels.isNotEmpty()) {
                _selectedChannel.value = channels.first()
            }
        }
        return result
    }

    fun selectChannel(channel: TelegramChannel) {
        _selectedChannel.value = channel
        _session.update { it.copy(channel = channel) }
    }

    /**
     * Executes the required official Telegram RTMP stream setup:
     * 1. Validates channel administrator permissions.
     * 2. Checks whether an RTMP livestream/group call is already active.
     * 3. If no RTMP livestream exists, creates one using phone.createGroupCall(rtmp_stream=true).
     * 4. Obtains the actual RTMP URL and secret stream key using phone.getGroupCallStreamRtmpUrl.
     * 5. Protects the stream key in memory without exposing it in logs.
     */
    suspend fun prepareTelegramLivestream(
        channel: TelegramChannel,
        streamTitle: String = "OBS Mobile Live"
    ): Result<TelegramRtmpCredentials> {
        updateLiveStatus(TelegramLiveStatus.CONNECTING, "Authenticating with channel...")

        // Permission check
        if (!channel.hasAdminRights || !channel.canManageCall) {
            val errorMsg = "CHAT_ADMIN_REQUIRED: You must be an administrator with Manage Video Chats permission."
            updateLiveStatus(TelegramLiveStatus.ERROR, errorMsg)
            return Result.failure(IllegalStateException(errorMsg))
        }

        // Step 1: Check active group call
        var activeCall = client.checkActiveGroupCall(channel).getOrNull()
        var createdByApp = false

        // Step 2: If no active call, create RTMP livestream
        if (activeCall == null) {
            updateLiveStatus(TelegramLiveStatus.CONNECTING, "Creating Telegram RTMP livestream...")
            val createResult = client.createRtmpGroupCall(channel, streamTitle)
            if (createResult.isFailure) {
                val err = createResult.exceptionOrNull()?.message ?: "Failed to create group call"
                updateLiveStatus(TelegramLiveStatus.ERROR, err)
                return Result.failure(createResult.exceptionOrNull()!!)
            }
            activeCall = createResult.getOrNull()
            createdByApp = true
        }

        // Step 3: Obtain RTMP URL and Secret Key
        updateLiveStatus(TelegramLiveStatus.CONNECTING, "Retrieving Telegram RTMP credentials...")
        val rtmpResult = client.getGroupCallStreamRtmpUrl(channel, revoke = false)
        if (rtmpResult.isFailure) {
            val err = rtmpResult.exceptionOrNull()?.message ?: "Failed to get RTMP stream URL"
            updateLiveStatus(TelegramLiveStatus.ERROR, err)
            return Result.failure(rtmpResult.exceptionOrNull()!!)
        }

        val rtmpInfo = rtmpResult.getOrThrow()
        // Wrap key in ProtectedStreamKey to guarantee it is NEVER logged
        val credentials = TelegramRtmpCredentials(
            rtmpUrl = rtmpInfo.url,
            streamKey = ProtectedStreamKey(rtmpInfo.key)
        )

        _session.update {
            it.copy(
                channel = channel,
                activeGroupCall = activeCall,
                credentials = credentials,
                status = TelegramLiveStatus.CONNECTED,
                statusMessage = "Telegram RTMP credentials obtained",
                isCreatedByApp = createdByApp
            )
        }

        updateLiveStatus(TelegramLiveStatus.CONNECTED, "Connected to Telegram RTMP endpoint")
        return Result.success(credentials)
    }

    /**
     * Verifies that the Telegram livestream is actually active on the server
     * before displaying TELEGRAM: LIVE.
     */
    suspend fun verifyAndSetLive(): Boolean {
        val currentCall = _session.value.activeGroupCall ?: return false
        val isActive = client.verifyGroupCallIsActive(currentCall)
        if (isActive) {
            updateLiveStatus(TelegramLiveStatus.LIVE, "Broadcasting Live to Telegram Channel")
            return true
        } else {
            updateLiveStatus(TelegramLiveStatus.ERROR, "Telegram channel livestream is not active")
            return false
        }
    }

    /**
     * Stops the live stream and discards the group call if requested.
     */
    suspend fun stopTelegramLive(discardGroupCall: Boolean = true) {
        val currentCall = _session.value.activeGroupCall
        if (discardGroupCall && currentCall != null) {
            client.discardGroupCall(currentCall)
        }
        _session.update {
            it.copy(
                activeGroupCall = null,
                credentials = null,
                status = TelegramLiveStatus.IDLE,
                statusMessage = null,
                isCreatedByApp = false
            )
        }
        updateLiveStatus(TelegramLiveStatus.IDLE)
    }
}
