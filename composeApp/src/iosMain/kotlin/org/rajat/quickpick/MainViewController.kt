package org.rajat.quickpick

import androidx.compose.ui.window.ComposeUIViewController
import org.rajat.quickpick.di.initializeKoin

fun MainViewController() = ComposeUIViewController {
    initializeKoin()
    App()
}