package org.rajat.quickpick

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
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
import org.rajat.quickpick.presentation.feature.auth.password.ForgotPasswordScreen
import org.rajat.quickpick.presentation.feature.auth.password.ResetPasswordOtpScreen
import org.rajat.quickpick.presentation.feature.auth.register.UserRegisterScreen
import org.rajat.quickpick.presentation.feature.auth.register.VendorRegisterScreen
import org.rajat.quickpick.presentation.feature.auth.verify.EmailOtpVerifyScreen
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
import org.rajat.quickpick.presentation.feature.profile.PrivacyPolicyScreen
import org.rajat.quickpick.presentation.feature.profile.ProfileScreen
import org.rajat.quickpick.presentation.feature.profile.SettingsScreen
import org.rajat.quickpick.presentation.feature.vendor.VendorScreen
import org.rajat.quickpick.presentation.feature.vendor.dashboard.VendorDashboardScreen
import org.rajat.quickpick.presentation.feature.vendor.menu.AddMenuItemScreen
import org.rajat.quickpick.presentation.feature.vendor.menu.UpdateMenuItemScreen
import org.rajat.quickpick.presentation.feature.vendor.menu.VendorMenuScreen
import org.rajat.quickpick.presentation.feature.vendor.orders.VendorOrderDetailScreen
import org.rajat.quickpick.presentation.feature.vendor.orders.VendorOrdersScreen
import org.rajat.quickpick.presentation.feature.vendor.profile.HelpAndSupportScreenVendor
import org.rajat.quickpick.presentation.feature.vendor.profile.VendorProfileScreen
import org.rajat.quickpick.presentation.feature.vendor.profile.VendorProfileUpdateScreen
import org.rajat.quickpick.presentation.feature.vendor.reviews.VendorReviewsScreen
import org.rajat.quickpick.presentation.feature.menuitem.MenuItemScreen
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.navigation.getAppScreenUserFromRoute
import org.rajat.quickpick.presentation.navigation.getAppScreenVendorFromRoute
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.presentation.viewmodel.HomeViewModel
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel
import org.rajat.quickpick.utils.tokens.RefreshTokenManager
import org.rajat.quickpick.utils.rememberImagePickerHelper
import org.rajat.quickpick.presentation.feature.notification.NotificationPermissionScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    val authViewModel: AuthViewModel = koinInject()
    val homeViewModel: HomeViewModel = koinInject()
    val vendorViewModel: VendorViewModel = koinInject()
    val menuItemViewModel: MenuItemViewModel = koinInject()
    val orderViewModel: OrderViewModel = koinInject()
    val dataStore: LocalDataStore = koinInject()
    val refreshTokenManager: RefreshTokenManager = koinInject()
    val reviewViewModel : ReviewViewModel = koinInject()
    val imagePickerHelper = rememberImagePickerHelper()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRouteString = backStackEntry?.destination?.route

    val currentRouteUser = getAppScreenUserFromRoute(currentRouteString)
    val currentRouteVendor = getAppScreenVendorFromRoute(currentRouteString)

    val onNavigate: (String) -> Unit = { routeString ->
        getAppScreenUserFromRoute(routeString)?.let { screen ->
            navController.navigate(screen) { launchSingleTop = true }
            return@let
        }
        getAppScreenVendorFromRoute(routeString)?.let { screen ->
            navController.navigate(screen) { launchSingleTop = true }
            return@let
        }
    }

    val showVendorBasePage = when (currentRouteVendor) {
        null -> false
        AppScreenVendor.VendorVerificationPending -> false
        else -> true
    }

    val showUserBasePage = when (currentRouteUser) {
        null -> false

        AppScreenUser.Splash,
        AppScreenUser.Onboarding1,
        AppScreenUser.Onboarding2,
        AppScreenUser.Onboarding3,
        AppScreenUser.NotificationPermission,
        AppScreenUser.Welcome,
        AppScreenUser.LaunchWelcome,
        AppScreenUser.UserLogin,
        AppScreenUser.VendorLogin,
        AppScreenUser.UserRegister,
        AppScreenUser.VendorRegister,
        AppScreenUser.EmailOtpVerify, // hide base UI on OTP screen
        AppScreenUser.ReviewOrderConfirmation,
        AppScreenUser.CancelOrderConfirmation,
        AppScreenUser.ConfirmOrder -> false

        else -> true
    }


    if (showVendorBasePage) {
        VendorBasePage(
            currentRoute = currentRouteString
                ?: AppScreenVendor.VendorDashboard::class.simpleName!!,
            onNavigate = onNavigate,
            onBackClick = { navController.popBackStack() }
        ) { padding ->
            AppNavHost(
                navController,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel,
                vendorViewModel = vendorViewModel,
                menuItemViewModel = menuItemViewModel,
                orderViewModel = orderViewModel,
                dataStore = dataStore,
                refreshTokenManager = refreshTokenManager,
                imagePickerHelper = imagePickerHelper,
                appPaddingValues = padding
            )
        }

    } else if (showUserBasePage) {
        BasePage(
            currentRoute = currentRouteString
                ?: AppScreenUser.HomeScreen::class.simpleName!!,
            onNavigate = onNavigate,
            onBackClick = { navController.popBackStack() }
        ) { padding ->
            AppNavHost(
                navController,
                authViewModel = authViewModel,
                homeViewModel = homeViewModel,
                vendorViewModel = vendorViewModel,
                menuItemViewModel = menuItemViewModel,
                orderViewModel = orderViewModel,
                dataStore = dataStore,
                refreshTokenManager = refreshTokenManager,
                imagePickerHelper = imagePickerHelper,
                appPaddingValues = padding
            )
        }

    } else {
        AppNavHost(
            navController,
            authViewModel = authViewModel,
            homeViewModel = homeViewModel,
            vendorViewModel = vendorViewModel,
            menuItemViewModel = menuItemViewModel,
            orderViewModel = orderViewModel,
            dataStore = dataStore,
            refreshTokenManager = refreshTokenManager,
            imagePickerHelper = imagePickerHelper,
            appPaddingValues = PaddingValues(0.dp)
        )
    }
}
@Composable
private fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    vendorViewModel: VendorViewModel,
    menuItemViewModel: MenuItemViewModel,
    orderViewModel: OrderViewModel,
    dataStore: LocalDataStore,
    refreshTokenManager: RefreshTokenManager,
    imagePickerHelper: org.rajat.quickpick.utils.ImagePickerHelper,
    appPaddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = AppScreenUser.Splash
    ) {
        composable<AppScreenUser.Splash> {
            SplashScreen(
                navController = navController,
                datastore = dataStore,
                authViewModel = authViewModel
            )
        }

        composable<AppScreenUser.Onboarding1> {
            OnboardingScreen1(
                navController = navController,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.Onboarding2> {
            OnboardingScreen2(
                navController = navController,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.Onboarding3> {
            OnboardingScreen3(
                navController = navController,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.NotificationPermission> {
            NotificationPermissionScreen(
                navController = navController,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.Welcome> {
            WelcomeScreen()
        }

        composable<AppScreenUser.LaunchWelcome> {
            LaunchWelcomeScreen(
                navController = navController
            )
        }

        composable<AppScreenUser.UserLogin> {
            UserLoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.VendorLogin> {
            VendorLoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.UserRegister> {
            UserRegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.VendorRegister> {
            VendorRegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.EmailOtpVerify> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenUser.EmailOtpVerify>()
            EmailOtpVerifyScreen(
                navController = navController,
                authViewModel = authViewModel,
                email = route.email,
                userType = route.userType,
                dataStore = dataStore
            )
        }

        composable<AppScreenUser.HomeScreen> {
            HomeScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                homeViewModel = homeViewModel,
                vendorViewModel = vendorViewModel,
                reviewViewModel = koinInject(),
                menuItemViewModel = menuItemViewModel
            )
        }

        composable<AppScreenUser.VendorDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenUser.VendorDetail>()
            VendorScreen(
                navController = navController,
                vendorViewModel = vendorViewModel,
                menuItemViewModel = menuItemViewModel,
                reviewViewModel = koinInject(),
                vendorId = route.vendorId
            )
        }

        composable<AppScreenUser.MenuItemCategory> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenUser.MenuItemCategory>()
            MenuItemScreen(
                navController = navController,
                menuItemViewModel = menuItemViewModel,
                vendorId = route.vendorId,
                category = route.category,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable<AppScreenVendor.VendorReviewsScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenVendor.VendorReviewsScreen>()
            VendorReviewsScreen(
                navController = navController,
                vendorId = route.vendorId
            )
        }

        // --- VENDOR SCREENS ---
        composable<AppScreenVendor.VendorDashboard> {
            VendorDashboardScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                orderViewModel = orderViewModel
            )
        }

        composable<AppScreenVendor.VendorOrders> {
            VendorOrdersScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                orderViewModel = orderViewModel
            )
        }

        composable<AppScreenVendor.VendorOrderDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenVendor.VendorOrderDetail>()
            VendorOrderDetailScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                orderId = route.orderId,
                orderViewModel = orderViewModel
            )
        }

        composable<AppScreenVendor.VendorMenu> {
            VendorMenuScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                menuItemViewModel = menuItemViewModel
            )
        }

        composable<AppScreenVendor.VendorProfile> {
            VendorProfileScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }
        composable<AppScreenVendor.VendorProfileUpdate> {
            VendorProfileUpdateScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                imagePickerHelper = imagePickerHelper
            )
        }
        composable<AppScreenVendor.VendorVerificationPending> {
            org.rajat.quickpick.presentation.feature.vendor.verification.VendorVerificationPendingScreen(
                navController = navController
            )
        }
        composable<AppScreenVendor.AddMenuItemScreen> {
            AddMenuItemScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                imagePickerHelper = imagePickerHelper
            )
        }
        composable<AppScreenVendor.UpdateMenuItemScreen> { backStackEntry->
            val route = backStackEntry.toRoute<AppScreenVendor.UpdateMenuItemScreen>()
            UpdateMenuItemScreen(
                navController = navController,
                paddingValues = appPaddingValues,
                menuItemId = route.orderId,
                imagePickerHelper = imagePickerHelper
            )
        }
        composable<AppScreenVendor.HelpAndSupportScreenVendor>{
            HelpAndSupportScreenVendor(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }

        // --- USER ORDER ROUTES ---
        composable<AppScreenUser.Orders> {
            MyOrderScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }

        composable<AppScreenUser.ReviewOrder> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenUser.ReviewOrder>()
            OrderReviewScreen(
                navController = navController,
                orderId = route.orderId,
                itemName = route.itemName,
                itemImageUrl = route.itemImageUrl,
                paddingValues = appPaddingValues
            )
        }

        composable<AppScreenUser.OrderDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenUser.OrderDetail>()
            OrderDetailScreen(
                navController = navController,
                orderId = route.orderId,
                paddingValues = appPaddingValues
            )
        }

        composable<AppScreenUser.CancelOrder> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenUser.CancelOrder>()
            CancelOrderScreen(
                navController = navController,
                orderId = route.orderId,
                paddingValues = appPaddingValues
            )
        }

        composable<AppScreenUser.ReviewOrderConfirmation> {
            ReviewOrderConfirmationScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }

        composable<AppScreenUser.CancelOrderConfirmation> {
            OrderCancelledConfirmationScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }

        // --- PROFILE SCREENS ---
        composable<AppScreenUser.Profile> {
            ProfileScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }

        composable<AppScreenUser.MyProfile> {
            MyProfileScreen(
                navController = navController,
                paddingValues = appPaddingValues
            )
        }

        composable<AppScreenUser.ContactUs> {
            ContactUsScreen(
                navController = navController,
                paddingValues = appPaddingValues,
            )
        }

        composable<AppScreenUser.ChangePassword> {
            ChangePasswordScreen(
                paddingValues = appPaddingValues,
                isLoading = false,
                navController = navController
            )
        }

        composable<AppScreenUser.NotificationSetting> {
            NotificationSettingsScreen(
                paddingValues = appPaddingValues,
                navController = navController)
        }

        composable<AppScreenUser.Settings> {
            SettingsScreen(
                paddingValues = appPaddingValues,
                navController = navController
            )
        }

        composable<AppScreenUser.PrivacyPolicy> {
            PrivacyPolicyScreen(
                navController = navController
            )
        }

        composable<AppScreenVendor.PrivacyPolicy> {
            PrivacyPolicyScreen(
                navController = navController
            )
        }

        composable<AppScreenUser.Cart> {
            CartScreen(
                paddingValues = appPaddingValues,
                navController = navController
            )
        }

        composable<AppScreenUser.Checkout> {
            CheckoutScreen(
                paddingValues = appPaddingValues,
                navController = navController
            )
        }

        composable<AppScreenUser.ConfirmOrder> { backStackEntry ->
            val confirmOrderRoute = backStackEntry.toRoute<AppScreenUser.ConfirmOrder>()
            val orderId = confirmOrderRoute.orderId
            OrderConfirmationScreen(
                paddingValues = appPaddingValues,
                navController = navController,
                orderId = orderId
            )
        }

        composable<AppScreenUser.HelpAndFaqs> {
            HelpAndFaqsScreen(
                paddingValues = appPaddingValues,
                navController = navController
            )
        }

        composable<AppScreenUser.ForgotPassword> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenUser.ForgotPassword>()
            ForgotPasswordScreen(
                navController = navController,
                authViewModel = authViewModel,
                userType = route.userType
            )
        }
        composable<AppScreenUser.ResetPasswordOtp> { backStackEntry ->
            val route = backStackEntry.toRoute<AppScreenUser.ResetPasswordOtp>()
            ResetPasswordOtpScreen(
                navController = navController,
                authViewModel = authViewModel,
                email = route.email,
                userType = route.userType
            )
        }
    }
}