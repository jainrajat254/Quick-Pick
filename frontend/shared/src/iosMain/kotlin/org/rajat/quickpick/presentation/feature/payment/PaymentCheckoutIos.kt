package org.rajat.quickpick.presentation.feature.payment

import co.touchlab.kermit.Logger

private val razorLogger = Logger.withTag("RAZORPAYDEBUG")


actual fun openRazorpayCheckout(platformActivity: Any, razorpayKeyId: String, razorpayOrderId: String, amountInPaise: Long?) {
    razorLogger.d { "openRazorpayCheckout called on iOS (noop) for order=$razorpayOrderId" }
}
