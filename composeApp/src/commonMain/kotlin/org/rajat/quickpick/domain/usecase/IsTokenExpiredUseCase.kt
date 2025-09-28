package org.rajat.quickpick.domain.usecase

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import kotlin.io.encoding.Base64
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class IsTokenExpiredUseCase {

    operator fun invoke(jwt: String?): Boolean {
        if (jwt.isNullOrBlank()) return true

        return try {
            val parts = jwt.split(".")
            if (parts.size != 3) return true

            val payloadJson = decodeBase64Url(parts[1])
            val payload = Json.parseToJsonElement(payloadJson).jsonObject

            val exp = payload["exp"]?.jsonPrimitive?.longOrNull ?: return true
            val currentTime = Clock.System.now().epochSeconds

            exp < currentTime
        } catch (e: Exception) {
            true
        }
    }

    private fun decodeBase64Url(base64: String): String {
        val normalized = base64
            .replace('-', '+')
            .replace('_', '/')
            .padEnd((base64.length + 3) / 4 * 4, '=')

        return Base64.decode(normalized).decodeToString()
    }
}