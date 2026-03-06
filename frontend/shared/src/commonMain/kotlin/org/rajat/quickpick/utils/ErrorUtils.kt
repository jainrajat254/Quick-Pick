package org.rajat.quickpick.utils

import co.touchlab.kermit.Logger

object ErrorUtils {

    private val logger = Logger.withTag("ErrorUtils")
    fun sanitizeError(raw: String?): String {
        if (raw.isNullOrBlank()) return "Something went wrong. Please try again."

        logger.e { "Backend error: $raw" }

        val lower = raw.lowercase()
        return when {
            "failed to connect" in lower || "connection refused" in lower || "connection timed out" in lower || "unable to resolve host" in lower || "host is down" in lower ->
                "We're having trouble connecting to servers. Please check your internet connection or try again later."
            "timeout" in lower ->
                "The request timed out. Please try again."
            else -> raw
        }
    }
}

