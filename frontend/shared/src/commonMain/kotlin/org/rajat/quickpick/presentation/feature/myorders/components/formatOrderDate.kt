package org.rajat.quickpick.presentation.feature.myorders.components

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
fun formatOrderDate(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val formatter = LocalDateTime.Format {
            dayOfMonth()
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            chars(", ")
            year()
            chars(" at ")
            hour()
            char(':')
            minute()
            char(' ')
            amPmMarker("AM", "PM")
        }
        formatter.format(localDateTime)
    } catch (e: Exception) {
        "Invalid Date"
    }
}