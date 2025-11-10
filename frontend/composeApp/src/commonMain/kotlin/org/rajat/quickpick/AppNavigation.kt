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
import org.rajat.quickpick.presentation.components.VendorBasePage
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
import org.rajat.quickpick.presentation.feature.cart.CartScreen
import org.rajat.quickpick.presentation.feature.cart.CheckoutScreen
import org.rajat.quickpick.presentation.feature.cart.OrderConfirmationScreen
import org.rajat.quickpick.presentation.feature.home.HomeScreen
import org.rajat.quickpick.presentation.feature.myorders.CancelOrderScreen
import org.rajat.quickpick.presentation.feature.myorders.MyOrderScreen
import org.rajat.quickpick.presentation.feature.myorders.OrderCancelledConfirmationScreen
import org.rajat.quickpick.presentation.feature.myorders.OrderDetailScreen
import org.rajat.quickpick.presentation.feature.myorders.OrderReviewScreen
import org.rajat.quickpick.presentation.feature.myorders.ReviewOrderConfirmationScreen
import org.rajat.quickpick.presentation.feature.profile.ChangePasswordScreen
import org.rajat.quickpick.presentation.feature.profile.ContactUsScreen
import org.rajat.quickpick.presentation.feature.profile.HelpAndFaqsScreen
import org.rajat.quickpick.presentation.feature.profile.MyProfileScreen
import org.rajat.quickpick.presentation.feature.profile.NotificationSettingsScreen
import org.rajat.quickpick.presentation.feature.profile.ProfileScreen
import org.rajat.quickpick.presentation.feature.profile.SettingsScreen
import org.rajat.quickpick.presentation.feature.vendor.VendorScreen
import org.rajat.quickpick.presentation.feature.vendor.dashboard.VendorDashboardScreen
import org.rajat.quickpick.presentation.feature.vendor.menu.VendorMenuScreen
import org.rajat.quickpick.presentation.feature.vendor.orders.VendorOrderDetailScreen
import org.rajat.quickpick.presentation.feature.vendor.orders.VendorOrdersScreen
import org.rajat.quickpick.presentation.feature.vendor.profile.VendorProfileScreen
import org.rajat.quickpick.presentation.navigation.Routes
import org.rajat.quickpick.presentation.navigation.VendorRoutes
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.presentation.viewmodel.HomeViewModel
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.tokens.RefreshTokenManager

@Composable
fun AppNavigation(
    navController: NavHostController,
) {
    val authViewModel: AuthViewModel = koinInject()
    val homeViewModel: HomeViewModel = koinInject()
    val vendorViewModel: VendorViewModel = koinInject()
    val menuItemViewModel: MenuItemViewModel = koinInject()
    val orderViewModel: OrderViewModel = koinInject()
    val dataStore: LocalDataStore = koinInject()
    val refreshTokenManager: RefreshTokenManager = koinInject()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.Splash.route

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
        Routes.ReviewOrderConfirmation.route,
        Routes.CancelOrderConfirmation.route,
        Routes.ConfirmOrder.route,
    )

    val vendorScreens = listOf(
        VendorRoutes.VendorDashboard.route,
        VendorRoutes.VendorOrders.route,
        VendorRoutes.VendorMenu.route,
        VendorRoutes.VendorProfile.route,
        "vendor_order_detail/{orderId}"
    )

    val isVendorScreen = vendorScreens.any { currentRoute.startsWith(it.split("{")[0]) }
    val showBasePage = currentRoute !in screensWithoutBasePage

    if (showBasePage) {
        if (isVendorScreen) {
            VendorBasePage(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            ) { paddingValues ->
                AppNavHost(navController, authViewModel, homeViewModel, vendorViewModel, menuItemViewModel, orderViewModel, dataStore, refreshTokenManager, paddingValues)
            }
        } else {
            BasePage(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            ) { paddingValues ->
                AppNavHost(navController, authViewModel, homeViewModel, vendorViewModel, menuItemViewModel, orderViewModel, dataStore, refreshTokenManager, paddingValues)
            }
        }
    } else {
        AppNavHost(navController, authViewModel, homeViewModel, vendorViewModel, menuItemViewModel, orderViewModel, dataStore, refreshTokenManager, PaddingValues())
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    vendorViewModel: org.rajat.quickpick.presentation.viewmodel.VendorViewModel,
    menuItemViewModel: org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel,
    orderViewModel: OrderViewModel,
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
            HomeScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                homeViewModel = homeViewModel,
                vendorViewModel = vendorViewModel,
                menuItemViewModel = menuItemViewModel
            )
        }

        composable("vendor_detail/{vendorId}") {
            VendorScreen(
                navController = navController,
                vendorViewModel = vendorViewModel,
                menuItemViewModel = menuItemViewModel,
                vendorId = "v1"
            )
        }

        composable(VendorRoutes.VendorDashboard.route) {
            VendorDashboardScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                orderViewModel = orderViewModel
            )
        }

        composable(VendorRoutes.VendorOrders.route) {
            VendorOrdersScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                orderViewModel = orderViewModel
            )
        }

        composable(VendorRoutes.VendorOrderDetail.route) {
            val backStackEntry = navController.currentBackStackEntry
            val orderId = backStackEntry?.arguments?.getString("orderId") ?: ""

            VendorOrderDetailScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                orderId = orderId,
                orderViewModel = orderViewModel
            )
        }

        composable(VendorRoutes.VendorMenu.route) {
            VendorMenuScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                menuItemViewModel = menuItemViewModel
            )
        }

        composable(VendorRoutes.VendorProfile.route) {
            VendorProfileScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }

        composable(Routes.Orders.route) {
            MyOrderScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }
        composable(Routes.ReviewOrder.route) {
            val backStackEntry = navController.currentBackStackEntry
            val orderId = backStackEntry?.arguments?.getString("orderId") ?: ""

            OrderReviewScreen(
                navController = navController,
                orderId = orderId,
                itemName = "",
                itemImageUrl = "",
                paddingValues = appPaddingValues
            )
        }
        composable(Routes.OrderDetail.route) {
            val backStackEntry = navController.currentBackStackEntry
            val orderId = backStackEntry?.arguments?.getString("orderId") ?: ""

            OrderDetailScreen(
                navController = navController,
                orderId = orderId,
                paddingValues = appPaddingValues
            )
        }
        composable(Routes.CancelOrder.route) {
            val backStackEntry = navController.currentBackStackEntry
            val orderId = backStackEntry?.arguments?.getString("orderId") ?: ""

            CancelOrderScreen(
                navController = navController,
                orderId = orderId,
                paddingValues = appPaddingValues
            )
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
        composable(Routes.MyProfile.route) {
            MyProfileScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }
        composable(Routes.ContactUs.route) {
            ContactUsScreen(
                navController=navController,
                paddingValues = appPaddingValues,
            )
        }
        composable(Routes.ChangePassword.route){
            ChangePasswordScreen(
                paddingValues = appPaddingValues,
                isLoading = false,
                navController = navController
            )
        }
        composable(Routes.NotificationSetting.route){
            NotificationSettingsScreen(
                paddingValues = appPaddingValues,
                navController=navController)
        }
        composable(Routes.Settings.route){
            SettingsScreen(
                paddingValues = appPaddingValues,
                navController = navController
            )
        }
        composable(Routes.Cart.route) {
            CartScreen(
                paddingValues = appPaddingValues,
                navController = navController
            )
        }
        composable(Routes.Checkout.route){
            CheckoutScreen(
                paddingValues = appPaddingValues,
                navController = navController
            )
        }
        composable(Routes.ConfirmOrder.route) {
            OrderConfirmationScreen(
                paddingValues = appPaddingValues,
                navController = navController,
                orderId = "Q123321UHS"
            )
        }
        composable(Routes.HelpAndFaqs.route){
            HelpAndFaqsScreen(
                paddingValues = appPaddingValues,
                navController = navController
            )
        }
    }
}