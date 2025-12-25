package org.rajat.quickpick.presentation.feature.payment

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getPlatformActivityForPayment(): Any? {
    val ctx = LocalContext.current
    return (ctx as? ComponentActivity)
}

