package com.example.telegram

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Telegram TL (Type Language) Binary Serializer and Deserializer
 * Compliant with official MTProto 2.0 specifications.
 */
class TLWriter {
    private val outputStream = ByteArrayOutputStream()

    fun writeInt32(value: Int): TLWriter {
        val buffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putInt(value)
        outputStream.write(buffer.array())
        return this
    }

    fun writeInt64(value: Long): TLWriter {
        val buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putLong(value)
        outputStream.write(buffer.array())
        return this
    }

    fun writeDouble(value: Double): TLWriter {
        val buffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN)
        buffer.putDouble(value)
        outputStream.write(buffer.array())
        return this
    }

    fun writeBool(value: Boolean): TLWriter {
        writeInt32(if (value) TelegramTL.CONSTRUCTOR_BOOL_TRUE else TelegramTL.CONSTRUCTOR_BOOL_FALSE)
        return this
    }

    fun writeBytes(bytes: ByteArray): TLWriter {
        val len = bytes.size
        if (len <= 253) {
            outputStream.write(len)
            outputStream.write(bytes)
            val padding = (4 - (len + 1) % 4) % 4
            for (i in 0 until padding) {
                outputStream.write(0)
            }
        } else {
            outputStream.write(254)
            outputStream.write(len and 0xFF)
            outputStream.write((len shr 8) and 0xFF)
            outputStream.write((len shr 16) and 0xFF)
            outputStream.write(bytes)
            val padding = (4 - (len) % 4) % 4
            for (i in 0 until padding) {
                outputStream.write(0)
            }
        }
        return this
    }

    fun writeString(str: String): TLWriter {
        return writeBytes(str.toByteArray(Charsets.UTF_8))
    }

    fun writeRaw(bytes: ByteArray): TLWriter {
        outputStream.write(bytes)
        return this
    }

    fun toByteArray(): ByteArray = outputStream.toByteArray()
}

class TLReader(private val inputStream: ByteArrayInputStream) {
    constructor(bytes: ByteArray) : this(ByteArrayInputStream(bytes))

    fun readInt32(): Int {
        val bytes = ByteArray(4)
        val read = inputStream.read(bytes)
        if (read < 4) throw IllegalStateException("Unexpected end of TL stream when reading Int32")
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).int
    }

    fun readInt64(): Long {
        val bytes = ByteArray(8)
        val read = inputStream.read(bytes)
        if (read < 8) throw IllegalStateException("Unexpected end of TL stream when reading Int64")
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).long
    }

    fun readDouble(): Double {
        val bytes = ByteArray(8)
        val read = inputStream.read(bytes)
        if (read < 8) throw IllegalStateException("Unexpected end of TL stream when reading Double")
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).double
    }

    fun readBool(): Boolean {
        val constructor = readInt32()
        return constructor == TelegramTL.CONSTRUCTOR_BOOL_TRUE
    }

    fun readBytes(): ByteArray {
        val first = inputStream.read()
        if (first == -1) throw IllegalStateException("Unexpected end of TL stream when reading bytes length")
        val len: Int
        var totalRead: Int
        if (first <= 253) {
            len = first
            totalRead = len + 1
        } else {
            val b1 = inputStream.read()
            val b2 = inputStream.read()
            val b3 = inputStream.read()
            len = b1 or (b2 shl 8) or (b3 shl 16)
            totalRead = len + 4
        }
        val bytes = ByteArray(len)
        var offset = 0
        while (offset < len) {
            val count = inputStream.read(bytes, offset, len - offset)
            if (count == -1) break
            offset += count
        }
        val padding = (4 - (totalRead % 4)) % 4
        if (padding > 0) {
            inputStream.skip(padding.toLong())
        }
        return bytes
    }

    fun readString(): String {
        return String(readBytes(), Charsets.UTF_8)
    }

    fun readRaw(length: Int): ByteArray {
        val bytes = ByteArray(length)
        val read = inputStream.read(bytes)
        if (read < length) throw IllegalStateException("Unexpected end of stream reading raw bytes")
        return bytes
    }

    val available: Int get() = inputStream.available()
}

/**
 * Core TL Constants, Constructors, and Schema Types
 */
object TelegramTL {
    const val CONSTRUCTOR_BOOL_FALSE = -1132882121 // 0xbc799737
    const val CONSTRUCTOR_BOOL_TRUE = -1720552011  // 0x997275b5
    const val CONSTRUCTOR_VECTOR = 481674261       // 0x1cb5c415
    const val CONSTRUCTOR_RPC_RESULT = -212030207  // 0xf35c6d01
    const val CONSTRUCTOR_RPC_ERROR = 558156304    // 0x2144ca10

    // Peers
    const val CONSTRUCTOR_INPUT_PEER_CHANNEL = 0x27bcbbfc.toInt() // 666680316
    const val CONSTRUCTOR_INPUT_CHANNEL = 0xf35aec8e.toInt()      // -212144946
    const val CONSTRUCTOR_INPUT_PEER_USER = 0xdde42493.toInt()
    const val CONSTRUCTOR_INPUT_PEER_SELF = 0x7da07ec9.toInt()

    // Group Calls & Channels
    const val CONSTRUCTOR_INPUT_GROUP_CALL = 0xd8aa840f.toInt()   // -660044785
    const val CONSTRUCTOR_PHONE_GET_GROUP_CALL_STREAM_RTMP_URL = 0x5af4c73a // 1525991226
    const val CONSTRUCTOR_PHONE_GET_GROUP_CALL_STREAM_RTMP_URL_L133 = 0xdeb3abbf.toInt()
    const val CONSTRUCTOR_PHONE_GROUP_CALL_STREAM_RTMP_URL = 0x2dbf3432 // 767505458
    const val CONSTRUCTOR_PHONE_CREATE_GROUP_CALL = 0x48cdc6d8    // 1221445336
    const val CONSTRUCTOR_PHONE_GET_GROUP_CALL = 0x041845db       // 68699611
    const val CONSTRUCTOR_PHONE_DISCARD_GROUP_CALL = 0x7835da49   // 2016803401
    const val CONSTRUCTOR_CHANNELS_GET_FULL_CHANNEL = 0x08736a09  // 141777417
    const val CONSTRUCTOR_CHANNELS_GET_ADMINED_PUBLIC_CHANNELS = 0xf8b036af.toInt()
    const val CONSTRUCTOR_MESSAGES_GET_DIALOGS = 0xa0f4cb74.toInt()

    // Auth
    const val CONSTRUCTOR_AUTH_SEND_CODE = 0xa677244f.toInt()
    const val CONSTRUCTOR_AUTH_SIGN_IN = 0x8d52a951.toInt()
    const val CONSTRUCTOR_AUTH_IMPORT_BOT_AUTHORIZATION = 0x67a3ff2c
}

data class TLInputPeerChannel(
    val channelId: Long,
    val accessHash: Long
) {
    fun serialize(writer: TLWriter) {
        writer.writeInt32(TelegramTL.CONSTRUCTOR_INPUT_PEER_CHANNEL)
        writer.writeInt64(channelId)
        writer.writeInt64(accessHash)
    }
}

data class TLInputChannel(
    val channelId: Long,
    val accessHash: Long
) {
    fun serialize(writer: TLWriter) {
        writer.writeInt32(TelegramTL.CONSTRUCTOR_INPUT_CHANNEL)
        writer.writeInt64(channelId)
        writer.writeInt64(accessHash)
    }
}

data class TLInputGroupCall(
    val id: Long,
    val accessHash: Long
) {
    fun serialize(writer: TLWriter) {
        writer.writeInt32(TelegramTL.CONSTRUCTOR_INPUT_GROUP_CALL)
        writer.writeInt64(id)
        writer.writeInt64(accessHash)
    }

    companion object {
        fun deserialize(reader: TLReader): TLInputGroupCall {
            val constructor = reader.readInt32()
            if (constructor != TelegramTL.CONSTRUCTOR_INPUT_GROUP_CALL) {
                throw IllegalArgumentException("Expected inputGroupCall constructor, got $constructor")
            }
            val id = reader.readInt64()
            val accessHash = reader.readInt64()
            return TLInputGroupCall(id, accessHash)
        }
    }
}

/**
 * Return type of phone.getGroupCallStreamRtmpUrl:
 * phone.groupCallStreamRtmpUrl#2dbf3432 url:string key:string = phone.GroupCallStreamRtmpUrl
 */
data class TLGroupCallStreamRtmpUrl(
    val url: String,
    val key: String
) {
    companion object {
        fun deserialize(reader: TLReader): TLGroupCallStreamRtmpUrl {
            val constructor = reader.readInt32()
            if (constructor != TelegramTL.CONSTRUCTOR_PHONE_GROUP_CALL_STREAM_RTMP_URL) {
                throw IllegalArgumentException("Expected phone.groupCallStreamRtmpUrl ($TelegramTL.CONSTRUCTOR_PHONE_GROUP_CALL_STREAM_RTMP_URL), got $constructor")
            }
            val url = reader.readString()
            val key = reader.readString()
            return TLGroupCallStreamRtmpUrl(url, key)
        }
    }
}

data class TLRpcError(
    val errorCode: Int,
    val errorMessage: String
) : Exception("Telegram RPC Error [$errorCode]: $errorMessage") {
    companion object {
        fun deserialize(reader: TLReader): TLRpcError {
            val code = reader.readInt32()
            val message = reader.readString()
            return TLRpcError(code, message)
        }
    }
}
