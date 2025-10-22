package org.rajat.quickpick.presentation.navigation

sealed class Routes(val route: String) {

    data object Splash : Routes("splash")
    data object Welcome : Routes("welcome")
    data object LaunchWelcome: Routes("launch_welcome")
    data object Onboarding1 : Routes("onboarding1")
    data object Onboarding2 : Routes("onboarding2")
    data object Onboarding3 : Routes("onboarding3")
    data object UserRegister : Routes("user_register")
    data object UserLogin : Routes("user_login")
    data object VendorRegister : Routes("vendor_register")
    data object VendorLogin : Routes("vendor_login")
    data object Home : Routes("home")
    data object VendorDetail : Routes("vendor_detail/{vendorId}") {
        fun createRoute(vendorId: String) = "vendor_detail/$vendorId"
    }
}