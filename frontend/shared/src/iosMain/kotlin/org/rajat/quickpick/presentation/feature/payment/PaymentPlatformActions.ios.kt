package org.rajat.quickpick.presentation.feature.payment

import co.touchlab.kermit.Logger
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

private val razorLogger = Logger.withTag("RAZORPAYDEBUG")

actual fun openExternalUrl(platformActivity: Any?, url: String) {
    try {
        razorLogger.d("openExternalUrl called on iOS with url=$url")
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) {
            val app = UIApplication.sharedApplication
            app.openURL(nsUrl)
            razorLogger.d("openExternalUrl: openURL invoked")
        } else {
            razorLogger.d("openExternalUrl: invalid NSURL for $url")
        }
    } catch (e: Throwable) {
        razorLogger.d("openExternalUrl: exception ${e.message}")
    }
}

