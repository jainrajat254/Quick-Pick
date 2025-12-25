package org.rajat.quickpick.presentation.feature.payment

import android.content.Intent
import android.net.Uri
import android.app.Activity

actual fun openExternalUrl(platformActivity: Any?, url: String) {
    try {
        val activity = platformActivity as? Activity
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (activity != null) {
            activity.startActivity(intent)
        }
    } catch (_: Exception) {
        // best-effort
    }
}

