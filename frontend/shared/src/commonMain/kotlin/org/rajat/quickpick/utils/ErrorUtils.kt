package org.rajat.quickpick.utils

import co.touchlab.kermit.Logger

object ErrorUtils {

    private val logger = Logger.withTag("ErrorUtils")
    fun sanitizeError(raw: String?): String {
        if (raw.isNullOrBlank()) return "Something went wrong. Please try again."

        logger.e { "Backend error: $raw" }

        val lower = raw.lowercase()
        return when {
            "failed to connect" in lower || "connection refused" in lower || "connection timed out" in lower || "unable to resolve host" in lower || "http 5" in lower || "host is down" in lower ->
                "We're having trouble connecting to servers. Please check your internet connection or try again later."
            "unauthorized" in lower || "forbidden" in lower || "401" in lower || "403" in lower ->
                "You're not authorized. Please sign in again."
            "not found" in lower || "404" in lower ->
                "Requested resource was not found. Please try again."
            "timeout" in lower ->
                "The request timed out. Please try again."
            "validation" in lower || "invalid" in lower || "bad request" in lower || "400" in lower ->
                "Please check the information provided and try again."
            else ->
                "Something went wrong. Please try again."
        }
    }
}

