package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.notification.NotificationPermissionScreen

@Composable
actual fun NotificationPermissionScreenWrapper(
    navController: NavHostController,
    dataStore: LocalDataStore
) {
    val logger = Logger.withTag("NotificationPermissionWrapper")

    NotificationPermissionScreen(
        navController = navController,
        dataStore = dataStore,
        onPermissionGranted = {
            logger.d { "iOS notification permission request" }
        }
    )
}
