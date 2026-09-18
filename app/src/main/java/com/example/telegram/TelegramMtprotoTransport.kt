package com.example.telegram

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Telegram MTProto 2.0 Client Transport
 * Implements Intermediate TCP transport over port 443 with official MTProto packet framing.
 */
class TelegramMtprotoTransport(
    private val dcIp: String = "149.154.167.50",
    private val dcPort: Int = 443
) {
    private var socket: Socket? = null
    private var inputStream: BufferedInputStream? = null
    private var outputStream: BufferedOutputStream? = null

    private var authKey: ByteArray? = null
    private var authKeyId: Long = 0L
    private var serverSalt: Long = 0L
    private val sessionId: Long = TelegramCrypto.randomInt64()
    private val seqNo = AtomicInteger(0)
    private val lastMsgId = AtomicLong(0)

    @Volatile
    var isConnected: Boolean = false
        private set

    suspend fun connect(timeoutMs: Int = 10000) = withContext(Dispatchers.IO) {
        try {
            disconnect()
            val sock = Socket()
            sock.connect(InetSocketAddress(dcIp, dcPort), timeoutMs)
            sock.soTimeout = timeoutMs
            sock.tcpNoDelay = true

            val out = BufferedOutputStream(sock.getOutputStream())
            val inp = BufferedInputStream(sock.getInputStream())

            // Intermediate transport initiation tag: 0xeeeeeeee
            out.write(byteArrayOf(0xee.toByte(), 0xee.toByte(), 0xee.toByte(), 0xee.toByte()))
            out.flush()

            socket = sock
            outputStream = out
            inputStream = inp
            isConnected = true
        } catch (e: Exception) {
            disconnect()
            throw e
        }
    }

    fun disconnect() {
        isConnected = false
        try { inputStream?.close() } catch (_: Exception) {}
        try { outputStream?.close() } catch (_: Exception) {}
        try { socket?.close() } catch (_: Exception) {}
        inputStream = null
        outputStream = null
        socket = null
    }

    fun setAuthKey(key: ByteArray, salt: Long = 0L) {
        require(key.size == 256) { "auth_key must be 256 bytes" }
        authKey = key
        val sha = TelegramCrypto.sha1(key)
        val buf = ByteBuffer.wrap(sha, 12, 8).order(ByteOrder.LITTLE_ENDIAN)
        authKeyId = buf.long
        serverSalt = salt
    }

    private fun generateMessageId(): Long {
        val now = System.currentTimeMillis()
        var msgId = (now / 1000L) shl 32
        msgId = msgId or ((now % 1000L) shl 21)
        val last = lastMsgId.get()
        if (msgId <= last) {
            msgId = last + 4
        }
        lastMsgId.set(msgId)
        return msgId
    }

    /**
     * Sends an MTProto request payload and reads the MTProto response.
     */
    suspend fun executeRpc(payload: ByteArray): ByteArray = withContext(Dispatchers.IO) {
        val out = outputStream ?: throw IllegalStateException("MTProto transport not connected")
        val inp = inputStream ?: throw IllegalStateException("MTProto transport not connected")

        val currentAuthKey = authKey
        val packetToSend: ByteArray

        if (currentAuthKey == null) {
            // Unencrypted MTProto message
            val msgId = generateMessageId()
            val writer = TLWriter()
            writer.writeInt64(0L) // auth_key_id = 0
            writer.writeInt64(msgId)
            writer.writeInt32(payload.size)
            writer.writeRaw(payload)
            packetToSend = writer.toByteArray()
        } else {
            // Encrypted MTProto 2.0 message
            val msgId = generateMessageId()
            val currentSeq = seqNo.getAndIncrement() * 2 + 1

            val innerWriter = TLWriter()
            innerWriter.writeInt64(serverSalt)
            innerWriter.writeInt64(sessionId)
            innerWriter.writeInt64(msgId)
            innerWriter.writeInt32(currentSeq)
            innerWriter.writeInt32(payload.size)
            innerWriter.writeRaw(payload)

            // MTProto 2.0 padding: 12..1024 bytes to multiple of 16
            val unpadded = innerWriter.toByteArray()
            val padLen = 16 - (unpadded.size % 16)
            val padding = TelegramCrypto.randomBytes(if (padLen < 12) padLen + 16 else padLen)
            val plaintextWithPadding = unpadded + padding

            val msgKey = TelegramCrypto.computeMsgKey(currentAuthKey, plaintextWithPadding, isClientToServer = true)
            val (aesKey, aesIv) = TelegramCrypto.computeAesKeyIv(currentAuthKey, msgKey, isClientToServer = true)
            val ciphertext = TelegramCrypto.aesIgeEncrypt(plaintextWithPadding, aesKey, aesIv)

            val packetWriter = TLWriter()
            packetWriter.writeInt64(authKeyId)
            packetWriter.writeRaw(msgKey)
            packetWriter.writeRaw(ciphertext)
            packetToSend = packetWriter.toByteArray()
        }

        // Intermediate transport framing: 4 bytes length (little-endian) + packet
        val lenBuf = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
        lenBuf.putInt(packetToSend.size)
        out.write(lenBuf.array())
        out.write(packetToSend)
        out.flush()

        // Read response length
        val respLenBytes = ByteArray(4)
        var read = 0
        while (read < 4) {
            val r = inp.read(respLenBytes, read, 4 - read)
            if (r == -1) throw IllegalStateException("Connection closed by Telegram DC while reading length")
            read += r
        }
        val respLen = ByteBuffer.wrap(respLenBytes).order(ByteOrder.LITTLE_ENDIAN).int
        if (respLen <= 0 || respLen > 2 * 1024 * 1024) {
            throw IllegalStateException("Invalid response length from Telegram DC: $respLen")
        }

        val respBytes = ByteArray(respLen)
        var totalRead = 0
        while (totalRead < respLen) {
            val r = inp.read(respBytes, totalRead, respLen - totalRead)
            if (r == -1) throw IllegalStateException("Connection closed by Telegram DC while reading packet body")
            totalRead += r
        }

        // Parse response
        if (currentAuthKey == null) {
            val reader = TLReader(respBytes)
            val respAuthKeyId = reader.readInt64()
            if (respAuthKeyId != 0L) throw IllegalStateException("Expected unencrypted authKeyId 0, got $respAuthKeyId")
            reader.readInt64() // msgId
            val dataLen = reader.readInt32()
            return@withContext reader.readRaw(dataLen)
        } else {
            val reader = TLReader(respBytes)
            val respAuthKeyId = reader.readInt64()
            val respMsgKey = reader.readRaw(16)
            val ciphertext = reader.readRaw(respBytes.size - 24)

            val (aesKey, aesIv) = TelegramCrypto.computeAesKeyIv(currentAuthKey, respMsgKey, isClientToServer = false)
            val plaintext = TelegramCrypto.aesIgeDecrypt(ciphertext, aesKey, aesIv)

            val plainReader = TLReader(plaintext)
            serverSalt = plainReader.readInt64()
            plainReader.readInt64() // sessionId
            plainReader.readInt64() // msgId
            plainReader.readInt32() // seqNo
            val dataLen = plainReader.readInt32()
            return@withContext plainReader.readRaw(dataLen)
        }
    }
}
