package com.project.dockin.util

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    data class Payload(
        val sub: String?,
        val auth: String?
    )

    fun parse(token: String): Payload? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val bodyBytes = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP)
            val json = JSONObject(String(bodyBytes))

            // 빈 문자열이면 null 로 바꾸기
            val sub  = json.optString("sub", "").ifEmpty { null }
            val auth = json.optString("auth", "").ifEmpty { null }

            Payload(sub = sub, auth = auth)
        } catch (_: Exception) {
            null
        }
    }
}