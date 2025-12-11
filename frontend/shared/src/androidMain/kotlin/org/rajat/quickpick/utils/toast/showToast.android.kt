package org.rajat.quickpick.utils.toast

import android.content.Context
import android.widget.Toast
import org.rajat.quickpick.QuickPickApplication

actual fun showToast(message: String?) {
    val context: Context = QuickPickApplication.provideAppContext()
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}