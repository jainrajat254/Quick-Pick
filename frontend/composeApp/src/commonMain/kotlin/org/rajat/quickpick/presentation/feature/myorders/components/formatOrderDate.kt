package org.rajat.quickpick.presentation.feature.myorders.components

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun formatOrderDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(dateString)

        val outputFormat = SimpleDateFormat("dd MMM, yyyy 'at' hh:mm a", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault() // Convert to local time
        date?.let { outputFormat.format(it) } ?: "Invalid Date"
    } catch (e: Exception) {
        "Invalid Date"
    }
}