package org.rajat.quickpick.presentation.feature

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
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

    LaunchedEffect(Unit) {
        val token = datastore.getToken()
        logger.d { "SplashScreen Token: $token" }

        when {
            token == null -> {
                logger.i { "No token found. Navigating to Login." }
                //NEED TO CHANGE THIS
                navController.navigate(Routes.Home.route) {
                    popUpTo(0) { inclusive = true }
                }
            }

            else -> {
                val valid = refreshTokenManager.ensureValidToken()
                if (valid) {
                    logger.i { "Token valid (or refreshed). Navigating to Home." }
                    navController.navigate(Routes.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    logger.i { "Token refresh failed. Navigating to Login." }
                    navController.navigate(Routes.GetOtpLogin.route) {
                        popUpTo(0) { inclusive = true }
                    }
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