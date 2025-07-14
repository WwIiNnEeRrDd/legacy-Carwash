package com.example.proyectofinalcarwash.ui.splash

import android.util.Base64
import org.json.JSONObject

fun isJwtExpired(token: String): Boolean {
    return try {
        val parts = token.split(".")
        if (parts.size != 3) return true

        val payloadJson = String(Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
        val payload = JSONObject(payloadJson)

        val exp = payload.getLong("exp")  // tiempo de expiración en segundos
        val now = System.currentTimeMillis() / 1000

        now >= exp
    } catch (e: Exception) {
        true // si hay error, asumimos que está expirado
    }
}