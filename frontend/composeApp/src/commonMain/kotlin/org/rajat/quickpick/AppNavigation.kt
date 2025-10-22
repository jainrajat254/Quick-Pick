package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.homescreen.HomeScreen
import org.rajat.quickpick.presentation.feature.SplashScreen
import org.rajat.quickpick.presentation.feature.register.UserRegisterScreen
import org.rajat.quickpick.presentation.feature.register.VendorRegisterScreen
import org.rajat.quickpick.presentation.feature.vendordetail.VendorScreen
import org.rajat.quickpick.presentation.navigation.Routes
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.utils.tokens.RefreshTokenManager

@Composable
fun AppNavigation(
    navController: NavHostController,
) {

    val authViewModel: AuthViewModel = koinInject()
    val dataStore: LocalDataStore = koinInject()
    val refreshTokenManager: RefreshTokenManager = koinInject()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(Routes.Splash.route) {
            SplashScreen(
                navController = navController,
                datastore = dataStore,
                refreshTokenManager = refreshTokenManager
            )
        }

        composable(Routes.UserRegister.route) {
            UserRegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                dataStore = dataStore
            )
        }

        composable(Routes.VendorRegister.route) {
            VendorRegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                dataStore = dataStore
            )
        }

        composable(Routes.Home.route) {
            HomeScreen(
                navController = navController
            )
        }

        composable("vendor_detail/{vendorId}") {

            VendorScreen(
                navController = navController,
                vendorId = "v1"
            )
        }

    }
}
