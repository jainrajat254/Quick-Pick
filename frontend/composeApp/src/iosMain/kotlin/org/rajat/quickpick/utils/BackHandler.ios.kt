package org.rajat.quickpick.utils

import androidx.compose.runtime.Composable
import kotlin.system.exitProcess

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {

}

actual fun exitApp() {
    exitProcess(0)
}

