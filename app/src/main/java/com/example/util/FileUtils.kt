package com.example.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.model.CustomMediaType

object FileUtils {

    fun queryFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            try {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        if (index != -1) {
                            result = it.getString(index)
                        }
                    }
                }
            } catch (_: Exception) {}
        }
        if (result.isNullOrEmpty()) {
            val path = uri.path
            val cut = path?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = path.substring(cut + 1)
            }
        }
        return result ?: "file_${System.currentTimeMillis()}"
    }

    fun detectMediaType(fileName: String, mimeType: String?): CustomMediaType {
        val lowerName = fileName.lowercase()
        return when {
            mimeType?.startsWith("video/") == true ||
            lowerName.endsWith(".mp4") ||
            lowerName.endsWith(".mkv") ||
            lowerName.endsWith(".mov") ||
            lowerName.endsWith(".webm") ||
            lowerName.endsWith(".avi") ||
            lowerName.endsWith(".3gp") -> CustomMediaType.VIDEO

            else -> CustomMediaType.IMAGE
        }
    }

    fun getFormatBadge(fileName: String?, mimeType: String?): String {
        val lower = fileName?.lowercase() ?: ""
        return when {
            lower.endsWith(".svg") || mimeType?.contains("svg") == true -> "SVG VECTOR"
            lower.endsWith(".gif") || mimeType?.contains("gif") == true -> "GIF ANIMATED"
            lower.endsWith(".png") || mimeType?.contains("png") == true -> "PNG TRANSPARENT"
            lower.endsWith(".jpg") || lower.endsWith(".jpeg") || mimeType?.contains("jpeg") == true -> "JPG PHOTO"
            lower.endsWith(".mp4") || mimeType?.contains("mp4") == true -> "MP4 VIDEO"
            lower.endsWith(".mkv") -> "MKV VIDEO"
            lower.endsWith(".mov") -> "MOV VIDEO"
            lower.endsWith(".webm") -> "WEBM VIDEO"
            mimeType?.startsWith("video/") == true -> "VIDEO FILE"
            mimeType?.startsWith("image/") == true -> "IMAGE FILE"
            else -> "MEDIA FILE"
        }
    }
}
