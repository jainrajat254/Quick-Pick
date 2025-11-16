package org.rajat.quickpick.utils.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun extractErrorMessage(json: String?): String {
    return try {
        val text = json ?: ""
        val jsonElement = Json.parseToJsonElement(text)
        val jsonObject = jsonElement.jsonObject
        val message = jsonObject["message"]?.jsonPrimitive?.content
        val fieldErrors = jsonObject["fieldErrors"]?.jsonArray
            ?.joinToString(", ") { it.jsonPrimitive.content }

        when {
            !fieldErrors.isNullOrBlank() -> fieldErrors
            !message.isNullOrBlank() -> message
            text.isNotBlank() -> text.trim()
            else -> "Unexpected error occurred"
        }
    } catch (e: Exception) {
        if (!json.isNullOrBlank()) json.trim() else "Unexpected error occurred"
    }
}