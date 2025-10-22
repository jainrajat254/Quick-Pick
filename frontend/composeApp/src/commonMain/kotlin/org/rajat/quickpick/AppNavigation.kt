package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.koin.compose.koinInject
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.SplashScreen
import org.rajat.quickpick.presentation.feature.auth.login.UserLoginScreen
import org.rajat.quickpick.presentation.feature.auth.login.VendorLoginScreen
import org.rajat.quickpick.presentation.feature.auth.onboarding.LaunchWelcomeScreen
import org.rajat.quickpick.presentation.feature.auth.onboarding.OnboardingScreen1
import org.rajat.quickpick.presentation.feature.auth.onboarding.OnboardingScreen2
import org.rajat.quickpick.presentation.feature.auth.onboarding.OnboardingScreen3
import org.rajat.quickpick.presentation.feature.auth.onboarding.WelcomeScreen
import org.rajat.quickpick.presentation.feature.auth.register.UserRegisterScreen
import org.rajat.quickpick.presentation.feature.auth.register.VendorRegisterScreen
import org.rajat.quickpick.presentation.feature.home.HomeScreen
import org.rajat.quickpick.presentation.feature.vendor.VendorScreen
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

        composable(Routes.Onboarding1.route) {
            OnboardingScreen1(
                navController = navController,
                dataStore = dataStore
            )
        }

        composable(Routes.Onboarding2.route) {
            OnboardingScreen2(
                navController = navController,
                dataStore = dataStore
            )
        }

        composable(Routes.Onboarding3.route) {
            OnboardingScreen3(
                navController = navController,
                dataStore = dataStore
            )
        }

        composable(Routes.Welcome.route) {
            WelcomeScreen()
        }

        composable(Routes.LaunchWelcome.route) {
            LaunchWelcomeScreen(
                navController = navController
            )
        }

        composable(Routes.UserLogin.route) {
            UserLoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                dataStore = dataStore
            )
        }

        composable(Routes.VendorLogin.route) {
            VendorLoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                dataStore = dataStore
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
            HomeScreen(navController = navController)
        }

        composable("vendor_detail/{vendorId}") {
            VendorScreen(
                navController = navController,
                vendorId = "v1"
            )
        }
    }
}