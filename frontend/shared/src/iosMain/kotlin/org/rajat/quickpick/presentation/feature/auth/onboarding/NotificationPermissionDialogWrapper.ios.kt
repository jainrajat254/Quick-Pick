package org.rajat.quickpick.presentation.feature.auth.onboarding

import androidx.compose.runtime.Composable
import co.touchlab.kermit.Logger
import org.rajat.quickpick.presentation.components.NotificationPermissionDialog

@Composable
actual fun NotificationPermissionDialogWrapper(
    onPermissionResult: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val logger = Logger.withTag("NotificationDialogWrapper")

    NotificationPermissionDialog(
        onAllowClick = {
            logger.d { "iOS: User clicked Allow" }
            onPermissionResult(true)
        },
        onDismiss = {
            logger.d { "iOS: User dismissed dialog" }
            onDismiss()
        }
    )
}

