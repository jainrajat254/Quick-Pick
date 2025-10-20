package org.rajat.quickpick.utils.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

fun extractErrorMessage(json: String?): String {
    return try {
        val jsonObject = Json.parseToJsonElement(json ?: "").jsonObject
        val message = jsonObject["message"]?.jsonPrimitive?.content
        val fieldErrors = jsonObject["fieldErrors"]?.jsonArray
            ?.joinToString(", ") { it.jsonPrimitive.content }

        when {
            !fieldErrors.isNullOrBlank() -> fieldErrors
            !message.isNullOrBlank() -> message
            else -> "Unexpected error occurred"
        }
    } catch (e: Exception) {
        "Unexpected error occurred"
    }
}