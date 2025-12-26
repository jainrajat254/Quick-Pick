package org.rajat.quickpick.presentation.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.auth.onboarding.WelcomeScreen
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.di.TokenProvider

@Composable
fun SplashScreen(
    navController: NavController,
    datastore: LocalDataStore,
    authViewModel: AuthViewModel
) {
    val logger = Logger.withTag("SPLASH LOGGER")
    val hasOnboarded by datastore.hasOnboarded.collectAsState(initial = false)

    LaunchedEffect(Unit) {
        delay(1500)

        val pendingEmail = datastore.getPendingVerificationEmail()
        val pendingUserType = datastore.getPendingVerificationUserType()
        if (!pendingEmail.isNullOrBlank() && !pendingUserType.isNullOrBlank()) {
            logger.i { "Pending verification found. Navigating to EmailOtpVerify." }
            navController.navigate(AppScreenUser.EmailOtpVerify(email = pendingEmail, userType = pendingUserType)) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        if (!hasOnboarded) {
            logger.i { "User has not onboarded. Navigating to onboarding." }
            navController.navigate(AppScreenUser.Onboarding1) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        val hasRequestedNotificationPermission = datastore.getHasRequestedNotificationPermission()

        if (!hasRequestedNotificationPermission) {
            navController.navigate(AppScreenUser.NotificationPermission) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        val accessToken = datastore.getToken()
        val refreshToken = datastore.getRefreshToken()

        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
            datastore.clearAll()
            navController.navigate(AppScreenUser.LaunchWelcome) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        val isValid = try {
            authViewModel.isSessionValid()
        } catch (e: Exception) {
            logger.e(e) { "Error checking session validity" }
            false
        }

        if (!isValid) {
            datastore.clearAll()
            TokenProvider.token = null
            navController.navigate(AppScreenUser.LaunchWelcome) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        val userRole = datastore.getUserRole()

        val destination = when (userRole) {
            "VENDOR" -> {
                AppScreenVendor.VendorDashboard
            }
            "USER" -> {
                AppScreenUser.HomeScreen
            }
            else -> {
                datastore.clearAll()
                TokenProvider.token = null
                navController.navigate(AppScreenUser.LaunchWelcome) {
                    popUpTo(0) { inclusive = true }
                }
                return@LaunchedEffect
            }
        }

        navController.navigate(destination) {
            popUpTo(0) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        WelcomeScreen()
    }
}
