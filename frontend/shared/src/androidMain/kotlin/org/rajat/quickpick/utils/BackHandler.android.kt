package org.rajat.quickpick.utils

import androidx.activity.compose.BackHandler as AndroidBackHandler
import androidx.compose.runtime.Composable
import kotlin.system.exitProcess

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    AndroidBackHandler(enabled = enabled, onBack = onBack)
}

actual fun exitApp() {
    exitProcess(0)
}

