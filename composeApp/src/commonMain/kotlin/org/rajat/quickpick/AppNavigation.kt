package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.rajat.quickpick.presentation.feature.DummyScreen
import org.rajat.quickpick.presentation.feature.SplashScreen
import org.rajat.quickpick.presentation.navigation.Routes

@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen()
//            DummyScreen()
        }
    }
}