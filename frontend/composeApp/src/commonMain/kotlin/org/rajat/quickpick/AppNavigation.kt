package org.rajat.quickpick

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import org.koin.compose.koinInject
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.components.BasePage
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
import org.rajat.quickpick.presentation.feature.myorders.CancelOrderScreen
import org.rajat.quickpick.presentation.feature.myorders.MyOrderScreen
import org.rajat.quickpick.presentation.feature.myorders.OrderCancelledConfirmationScreen
import org.rajat.quickpick.presentation.feature.myorders.OrderDetailScreen
import org.rajat.quickpick.presentation.feature.myorders.OrderReviewScreen
import org.rajat.quickpick.presentation.feature.myorders.ReviewOrderConfirmationScreen
import org.rajat.quickpick.presentation.feature.myorders.allOrders
import org.rajat.quickpick.presentation.feature.myorders.dummyActiveOrders
import org.rajat.quickpick.presentation.feature.myorders.dummyCancelledOrders
import org.rajat.quickpick.presentation.feature.myorders.dummyCompletedOrders
import org.rajat.quickpick.presentation.feature.profile.ProfileScreen
import org.rajat.quickpick.presentation.feature.profile.components.PlaceholderScreen
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.Splash.route

    // List of screens that should NOT show the BasePage (Scaffold, TopBar, BottomBar)
    val screensWithoutBasePage = listOf(
        Routes.Splash.route,
        Routes.Onboarding1.route,
        Routes.Onboarding2.route,
        Routes.Onboarding3.route,
        Routes.Welcome.route,
        Routes.LaunchWelcome.route,
        Routes.UserLogin.route,
        Routes.VendorLogin.route,
        Routes.UserRegister.route,
        Routes.VendorRegister.route,
        Routes.ReviewOrderConfirmation.route, // Confirmation screens often hide nav
        Routes.CancelOrderConfirmation.route
    )

    val showBasePage = currentRoute !in screensWithoutBasePage

    if (showBasePage) {
        // --- MAIN APP (with Scaffold, TopBar, BottomBar) ---
        BasePage(
            currentRoute = currentRoute,
            onNavigate = { route ->
                navController.navigate(route)
            },
            onBackClick = {
                navController.popBackStack()
            }
        ) { paddingValues ->
            AppNavHost(navController, authViewModel, dataStore, refreshTokenManager, paddingValues)
        }
    } else {
        AppNavHost(navController, authViewModel, dataStore, refreshTokenManager, PaddingValues())
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    dataStore: LocalDataStore,
    refreshTokenManager: RefreshTokenManager,
    appPaddingValues: PaddingValues
) {
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
            HomeScreen(navController = navController,
                paddingValues = appPaddingValues)
        }

        composable("vendor_detail/{vendorId}") {
            VendorScreen(
                navController = navController,
                vendorId = "v1"
            )
        }
        composable(Routes.Orders.route) {
            MyOrderScreen(
                navController = navController,
                activeOrders = dummyActiveOrders,
                completedOrders = dummyCompletedOrders,
                cancelledOrders = dummyCancelledOrders,
                isLoading = false,
                paddingValues = appPaddingValues
            )
        }
        composable(Routes.ReviewOrder.route) {
            val orderId = "1"
            val order = allOrders.find { it.id == orderId }
            if (order != null) {
                val itemName = order.orderItems?.firstOrNull()?.menuItemName ?: "Unknown Item"
                val orderIdStr = order.id ?: "Unknown ID"
                OrderReviewScreen(
                    navController = navController,
                    orderId = orderIdStr,
                    itemName = itemName,
                    itemImageUrl = "",
                    paddingValues = appPaddingValues
                )
            } else {
                PlaceholderScreen(name = "Order not found", paddingValues = appPaddingValues)
            }
        }
        composable(Routes.OrderDetail.route) {
            val orderId = "1"
            val orderToShow = allOrders.find { it.id == orderId }
            if (orderToShow != null) {
                OrderDetailScreen(
                    navController = navController,
                    order = orderToShow,
                    isLoading = false,
                    paddingValues = appPaddingValues
                )
            } else {
                PlaceholderScreen(name = "Order not found", paddingValues = appPaddingValues)
            }
        }
        composable(Routes.CancelOrder.route) {
            val orderID = "1"
            val order = allOrders.find { it.id == orderID }
            if (order != null) {
                CancelOrderScreen(
                    navController = navController,
                    orderId = order.id.toString(),
                    isLoading = false,
                    paddingValues = appPaddingValues
                )

            }
        }
        composable(Routes.ReviewOrderConfirmation.route) {
            ReviewOrderConfirmationScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }
        composable(Routes.CancelOrderConfirmation.route) {
            OrderCancelledConfirmationScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }
        composable(Routes.Profile.route) {
            ProfileScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }
    }
}