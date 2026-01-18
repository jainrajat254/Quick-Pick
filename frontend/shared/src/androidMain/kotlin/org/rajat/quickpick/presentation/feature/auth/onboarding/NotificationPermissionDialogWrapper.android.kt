package org.rajat.quickpick.presentation.feature.auth.onboarding

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import co.touchlab.kermit.Logger
import org.rajat.quickpick.presentation.components.NotificationPermissionDialog
import org.rajat.quickpick.utils.permissions.rememberNotificationPermissionState

@Composable
actual fun NotificationPermissionDialogWrapper(
    onPermissionResult: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val logger = Logger.withTag("NotificationDialogWrapper")
    var hasRequestedPermission by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(true) }

    val permissionState = rememberNotificationPermissionState { isGranted ->
        logger.d { "Permission callback received: isGranted=$isGranted" }
        hasRequestedPermission = true
        showDialog = false
        onPermissionResult(isGranted)
    }

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        LaunchedEffect(Unit) {
            logger.d { "SDK < 33, permission not needed, treating as granted" }
            onPermissionResult(true)
        }
        return
    }

    if (permissionState.isPermissionGranted() && !hasRequestedPermission) {
        LaunchedEffect(Unit) {
            logger.d { "Permission already granted" }
            onPermissionResult(true)
        }
        return
    }

    if (showDialog && !hasRequestedPermission) {
        NotificationPermissionDialog(
            onAllowClick = {
                logger.d { "User clicked Allow, launching permission request" }
                permissionState.launchPermissionRequest()
            },
            onDismiss = {
                logger.d { "User dismissed dialog" }
                showDialog = false
                onDismiss()
            }
        )
    }
}

