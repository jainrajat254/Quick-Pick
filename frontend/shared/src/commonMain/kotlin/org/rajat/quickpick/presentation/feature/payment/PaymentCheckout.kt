package org.rajat.quickpick.presentation.feature.payment

import co.touchlab.kermit.Logger

private val razorLogger = Logger.withTag("RAZORPAYDEBUG")

expect fun openRazorpayCheckout(platformActivity: Any, razorpayKeyId: String, razorpayOrderId: String, amountInPaise: Long? = null)

// Note: platformActivity may be provided via getPlatformActivityForPayment() in Compose.
// Implementations should log using tag RAZORPAYDEBUG for debugging.
