package com.project.dockin.util

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    data class Payload(
        val sub: String?,
        val auth: String?
    )

    fun parse(token: String): Payload? = try {
        val parts = token.split(".")
        if (parts.size < 2) {return null}
        val bodyBytes = Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP)
        val json = JSONObject(String(bodyBytes))
        Payload(
            sub  = json.optString("sub", null),
            auth = json.optString("auth", null)
        )
    } catch (_: Exception) {
        null
    }
}