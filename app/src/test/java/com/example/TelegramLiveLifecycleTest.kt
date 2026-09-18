package com.example

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.telegram.*
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36])
class TelegramLiveLifecycleTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testProtectedStreamKeySecurity() {
        val secret = "s_secret_tg_key_9831412"
        val protectedKey = ProtectedStreamKey(secret)

        // Must never print or expose secret in toString()
        assertEquals("[PROTECTED_TELEGRAM_STREAM_KEY]", protectedKey.toString())
        assertFalse(protectedKey.toString().contains(secret))

        // Must allow retrieval only via explicit getSecret()
        assertEquals(secret, protectedKey.getSecret())
    }

    @Test
    fun testTelegramTLBinarySerialization() {
        // Test primitive types serialization
        val writer = TLWriter()
        writer.writeInt32(123456)
        writer.writeInt64(9876543210123L)
        writer.writeBool(true)
        writer.writeString("telegram_rtmp_test")

        val bytes = writer.toByteArray()
        assertTrue(bytes.isNotEmpty())

        val reader = TLReader(bytes)
        assertEquals(123456, reader.readInt32())
        assertEquals(9876543210123L, reader.readInt64())
        assertTrue(reader.readBool())
        assertEquals("telegram_rtmp_test", reader.readString())
    }

    @Test
    fun testTelegramTLFunctionsConstructors() {
        // Test phone.getGroupCallStreamRtmpUrl binary request payload
        val peer = TLInputGroupCall(id = 8847192L, accessHash = 192837465L)
        val writer = TLWriter()
        writer.writeInt32(TelegramTL.CONSTRUCTOR_PHONE_GET_GROUP_CALL_STREAM_RTMP_URL)
        peer.serialize(writer)
        writer.writeBool(false) // revoke = false

        val payload = writer.toByteArray()
        val reader = TLReader(payload)
        assertEquals(TelegramTL.CONSTRUCTOR_PHONE_GET_GROUP_CALL_STREAM_RTMP_URL, reader.readInt32())
        val deserializedPeer = TLInputGroupCall.deserialize(reader)
        assertEquals(8847192L, deserializedPeer.id)
        assertEquals(192837465L, deserializedPeer.accessHash)
        assertFalse(reader.readBool())
    }

    @Test
    fun testTelegramCryptoIgeRoundTrip() {
        val key = ByteArray(32) { (it * 3).toByte() }
        val iv = ByteArray(32) { (it * 5).toByte() }
        val plaintext = "Official MTProto 2.0 Encrypted Stream Data Channel Ingest Payload 12345".toByteArray(Charsets.UTF_8)

        val encrypted = TelegramCrypto.aesIgeEncrypt(plaintext, key, iv)
        assertEquals(0, encrypted.size % 16) // AES block alignment

        val decrypted = TelegramCrypto.aesIgeDecrypt(encrypted, key, iv)
        val decryptedText = String(decrypted, Charsets.UTF_8).substring(0, plaintext.size)
        assertEquals("Official MTProto 2.0 Encrypted Stream Data Channel Ingest Payload 12345", decryptedText)
    }

    @Test
    fun testTelegramManagerLifecycleStates() {
        // Test initial status
        TelegramManager.updateLiveStatus(TelegramLiveStatus.IDLE)
        assertEquals(TelegramLiveStatus.IDLE, TelegramManager.liveStatus.value)

        // Status transition sequence
        TelegramManager.updateLiveStatus(TelegramLiveStatus.CONNECTING, "Contacting MTProto DC...")
        assertEquals(TelegramLiveStatus.CONNECTING, TelegramManager.liveStatus.value)

        TelegramManager.updateLiveStatus(TelegramLiveStatus.CONNECTED, "RTMP endpoint received")
        assertEquals(TelegramLiveStatus.CONNECTED, TelegramManager.liveStatus.value)

        TelegramManager.updateLiveStatus(TelegramLiveStatus.LIVE)
        assertEquals(TelegramLiveStatus.LIVE, TelegramManager.liveStatus.value)

        TelegramManager.updateLiveStatus(TelegramLiveStatus.ERROR, "CHAT_ADMIN_REQUIRED")
        assertEquals(TelegramLiveStatus.ERROR, TelegramManager.liveStatus.value)
        assertEquals("CHAT_ADMIN_REQUIRED", TelegramManager.session.value.statusMessage)

        TelegramManager.updateLiveStatus(TelegramLiveStatus.IDLE)
    }

    @Test
    fun testTelegramChannelAdminPermissions() {
        val adminChannel = TelegramChannel(
            id = 123456789L,
            accessHash = 987654321L,
            title = "Tech Announcements Live",
            username = "tech_live",
            canManageCall = true,
            activeGroupCallId = null
        )
        assertTrue(adminChannel.canManageCall)

        val nonAdminChannel = TelegramChannel(
            id = 999999999L,
            accessHash = 111111111L,
            title = "Read Only Public",
            username = "readonly_channel",
            canManageCall = false,
            activeGroupCallId = null
        )
        assertFalse(nonAdminChannel.canManageCall)
    }

    @Test
    fun testTelegramLiveHeaderUiInteraction() {
        composeRule.waitForIdle()

        // Verify Telegram live pill button exists on the header bar
        composeRule.onNodeWithTag("header_telegram_live_btn").assertExists()

        // Click to open Telegram Live Dialog
        composeRule.onNodeWithTag("header_telegram_live_btn").performClick()
        composeRule.waitForIdle()

        // Verify dialog is open
        composeRule.onNodeWithTag("telegram_live_dialog").assertExists()
        composeRule.onNodeWithTag("close_tg_dialog_btn").assertExists()

        // Dismiss dialog
        composeRule.onNodeWithTag("close_tg_dialog_btn").performClick()
        composeRule.waitForIdle()
    }
}
