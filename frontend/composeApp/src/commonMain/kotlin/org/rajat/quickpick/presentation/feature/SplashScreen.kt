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
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.navigation.Routes
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
            logger.i { "No token. Go to LaunchWelcome." }
            navController.navigate(Routes.LaunchWelcome.route) {
                popUpTo(0) { inclusive = true }
            }
        } else {
            val valid = refreshTokenManager.ensureValidToken()
            if (valid) {
                logger.i { "Token valid. Navigate to Home." }
                navController.navigate(Routes.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            } else {
                logger.i { "Token refresh failed. Go to LaunchWelcome." }
                navController.navigate(Routes.LaunchWelcome.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CustomLoader()
    }
}

