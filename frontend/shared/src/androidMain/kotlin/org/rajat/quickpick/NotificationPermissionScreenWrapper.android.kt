package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.notification.NotificationPermissionScreen
import org.rajat.quickpick.utils.permissions.rememberNotificationPermissionState

@Composable
actual fun NotificationPermissionScreenWrapper(
    navController: NavHostController,
    dataStore: LocalDataStore
) {
    val logger = Logger.withTag("NotificationImplementation")
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasRequestedOnce by remember { mutableStateOf(false) }
    var hasOpenedSettings by remember { mutableStateOf(false) }
    var shouldNavigateOnResume by remember { mutableStateOf(false) }

    val permissionState = rememberNotificationPermissionState { isGranted ->
        logger.d { "Permission result callback: isGranted=$isGranted" }
        hasRequestedOnce = true
        if (isGranted) {
            logger.i { "Notification permission GRANTED" }
        } else {
            logger.w { "Notification permission DENIED" }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && shouldNavigateOnResume) {

                val isGranted = permissionState.isPermissionGranted()
                logger.i { "Permission status after returning: ${if (isGranted) "GRANTED" else "DENIED"}" }

                scope.launch {
                    logger.d { "Marking notification permission as requested and navigating..." }
                    dataStore.setHasRequestedNotificationPermission(true)

                    val token = dataStore.getToken()
                    val userRole = dataStore.getUserRole()

                    logger.d { "Token exists: ${!token.isNullOrEmpty()}, UserRole: $userRole" }

                    if (!token.isNullOrEmpty()) {
                        when (userRole) {
                            "VENDOR" -> {
                                logger.d { "Navigating to VendorDashboard" }
                                navController.navigate(org.rajat.quickpick.presentation.navigation.AppScreenVendor.VendorDashboard) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                            "USER" -> {
                                logger.d { "Navigating to HomeScreen" }
                                navController.navigate(org.rajat.quickpick.presentation.navigation.AppScreenUser.HomeScreen) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                            else -> {
                                logger.d { "Invalid role, navigating to LaunchWelcome" }
                                navController.navigate(org.rajat.quickpick.presentation.navigation.AppScreenUser.LaunchWelcome) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    } else {
                        logger.d { "No token found, navigating to LaunchWelcome" }
                        navController.navigate(org.rajat.quickpick.presentation.navigation.AppScreenUser.LaunchWelcome) {
                            popUpTo(0) { inclusive = true }
                        }
                    }

                    shouldNavigateOnResume = false
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    NotificationPermissionScreen(
        navController = navController,
        dataStore = dataStore,
        onPermissionGranted = {
            logger.d { "hasRequestedOnce: $hasRequestedOnce, hasOpenedSettings: $hasOpenedSettings" }

            if (hasOpenedSettings) {
                logger.w { "Settings already opened, ignoring duplicate click" }
                return@NotificationPermissionScreen
            }

            logger.i { ">>> Opening app settings <<<" }
            hasOpenedSettings = true
            shouldNavigateOnResume = true
            permissionState.openAppSettings()
            logger.i { "Settings opened, will navigate on app resume" }
        },
        suppressAutoNavigate = true
    )
}
