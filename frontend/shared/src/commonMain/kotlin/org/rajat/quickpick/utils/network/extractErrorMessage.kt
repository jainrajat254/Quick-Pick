package org.rajat.quickpick.utils.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun extractErrorMessage(json: String?): String {
    return try {
        val text = json ?: ""
        val jsonElement = Json.parseToJsonElement(text)
        val jsonObject = jsonElement.jsonObject
        val message = jsonObject["message"]?.jsonPrimitive?.contentOrNull
        val fieldErrors = jsonObject["fieldErrors"]?.jsonArray
            ?.joinToString(", ") { it.jsonPrimitive.content }

        val attempts = jsonObject["attempts"]?.jsonPrimitive?.intOrNull
        val blocked = jsonObject["blocked"]?.jsonPrimitive?.booleanOrNull

        val baseMessage = when {
            !fieldErrors.isNullOrBlank() -> fieldErrors
            !message.isNullOrBlank() -> message
            text.isNotBlank() -> text.trim()
            else -> "Unexpected error occurred"
        }

        when {
            blocked == true -> "$baseMessage (blocked)"
            attempts != null -> "$baseMessage (attempts: $attempts)"
            else -> baseMessage
        }
    } catch (_: Exception) {
        if (!json.isNullOrBlank()) json.trim() else "Unexpected error occurred"
    }
}

private val kotlinx.serialization.json.JsonElement?.contentOrNull: String?
    get() = try { this?.jsonPrimitive?.content } catch (_: Exception) { null }

private val kotlinx.serialization.json.JsonElement?.intOrNull: Int?
    get() = try { this?.jsonPrimitive?.int } catch (_: Exception) { null }

private val kotlinx.serialization.json.JsonElement?.booleanOrNull: Boolean?
    get() = try { this?.jsonPrimitive?.boolean } catch (_: Exception) { null }
