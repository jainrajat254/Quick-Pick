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
import kotlinx.coroutines.launch
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.auth.onboarding.WelcomeScreen
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.utils.tokens.RefreshTokenManager

@Composable
fun SplashScreen(
    navController: NavController,
    datastore: LocalDataStore,
    refreshTokenManager: RefreshTokenManager
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

        val token = datastore.getToken()
        logger.d { "SplashScreen Token: $token" }

        val logoutLogger = Logger.withTag("LOGOUT_DEBUG")
        logoutLogger.d { "SPLASH - Checking auto-login" }
        logoutLogger.d { "SPLASH - Token: $token" }

        if (token.isNullOrEmpty()) {
            logger.i { "No token found. Navigating to LaunchWelcome." }
            logoutLogger.d { "SPLASH - No token found, navigating to LaunchWelcome" }
            navController.navigate(AppScreenUser.LaunchWelcome) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            val userRole = datastore.getUserRole()
            val userId = datastore.getId()
            val refreshToken = datastore.getRefreshToken()

            logoutLogger.d { "SPLASH - UserRole: $userRole" }
            logoutLogger.d { "SPLASH - UserId: $userId" }
            logoutLogger.d { "SPLASH - RefreshToken: $refreshToken" }

            val destination = when (userRole) {
                "VENDOR" -> {
                    logoutLogger.d { "SPLASH - Auto-logging in as VENDOR" }
                    AppScreenVendor.VendorDashboard
                }
                "USER" -> {
                    logoutLogger.d { "SPLASH - Auto-logging in as USER" }
                    AppScreenUser.HomeScreen
                }
                else -> {
                    logoutLogger.d { "SPLASH - Invalid role, clearing datastore" }
                    datastore.clearAll()
                    navController.navigate(AppScreenUser.LaunchWelcome) {
                        popUpTo(0) { inclusive = true }
                    }
                    return@LaunchedEffect
                }
            }

            logoutLogger.d { "SPLASH - Navigating to: $destination" }
            navController.navigate(destination) {
                popUpTo(0) { inclusive = true }
            }

            launch {
                try {
                    val refreshed = refreshTokenManager.ensureValidToken()
                    if (refreshed) {
                        logger.i { "Background token refresh successful." }
                    } else {
                        logger.w { "Background token refresh failed." }
                    }
                } catch (e: Exception) {
                    logger.e(e) { "Error during background token refresh." }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        WelcomeScreen()
    }
}
