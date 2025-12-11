package org.rajat.quickpick.presentation.feature.cart.components

import kotlin.math.round

fun formatPrice(price: Double): String {
    val rounded = round(price * 100) / 100
    return "₹%.2f".replace("%.2f", rounded.toString())
}
fun Double.formatTwoDecimals(): String {
    val rounded = round(this * 100) / 100
    val str = rounded.toString()
    return if (str.contains('.')) {
        val decimals = str.substringAfter('.')
        if (decimals.length == 1) str + "0" else str
    } else {
        "$str.00"
    }
}