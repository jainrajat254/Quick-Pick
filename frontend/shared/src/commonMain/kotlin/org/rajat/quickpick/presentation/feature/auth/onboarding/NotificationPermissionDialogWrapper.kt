package org.rajat.quickpick.presentation.feature.auth.onboarding

import androidx.compose.runtime.Composable

@Composable
expect fun NotificationPermissionDialogWrapper(
    onPermissionResult: (Boolean) -> Unit,
    onDismiss: () -> Unit
)

