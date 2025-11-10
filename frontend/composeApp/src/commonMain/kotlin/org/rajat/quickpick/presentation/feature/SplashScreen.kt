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
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.feature.auth.onboarding.WelcomeScreen
import org.rajat.quickpick.presentation.navigation.Routes
import org.rajat.quickpick.presentation.navigation.VendorRoutes
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

        if (!hasOnboarded) {
            logger.i { "User has not onboarded. Navigating to onboarding." }
            navController.navigate(Routes.Onboarding1.route) {
                popUpTo(0) { inclusive = true }
            }
            return@LaunchedEffect
        }

        val token = datastore.getToken()
        logger.d { "SplashScreen Token: $token" }

        if (token.isNullOrEmpty()) {
            logger.i { "No token found. Navigating to LaunchWelcome." }
            navController.navigate(Routes.LaunchWelcome.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            val userRole = datastore.getUserRole()

            val destination = when (userRole) {
                "VENDOR" -> {
                    VendorRoutes.VendorDashboard.route
                }
                "USER" -> {
                    Routes.Home.route
                }
                else -> {
                    datastore.clearAll()
                    navController.navigate(Routes.LaunchWelcome.route) {
                        popUpTo(0) { inclusive = true }
                    }
                    return@LaunchedEffect
                }
            }

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
