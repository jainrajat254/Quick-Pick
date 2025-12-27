package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.notification.NotificationPermissionScreen
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

@Composable
actual fun NotificationPermissionScreenWrapper(
    navController: NavHostController,
    dataStore: LocalDataStore
) {
    val logger = Logger.withTag("NotificationPermissionWrapper")

    fun openAppSettings() {
        try {
            val url = NSURL.URLWithString(UIApplicationOpenSettingsURLString)
            if (url != null) {
                UIApplication.sharedApplication.openURL(url)
                logger.i { "Opened iOS app settings" }
            } else {
                logger.e { "Failed to create URL for app settings" }
            }
        } catch (t: Throwable) {
            logger.e(t) { "Error while trying to open app settings" }
        }
    }

    NotificationPermissionScreen(
        navController = navController,
        dataStore = dataStore,
        onPermissionGranted = {
            logger.d { "iOS: opening app settings for notifications" }
            openAppSettings()
        },
        suppressAutoNavigate = true
    )
}
