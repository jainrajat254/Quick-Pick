package org.rajat.quickpick.presentation.navigation

sealed class Routes(val route: String) {

    data object Splash : Routes("splash")
    data object Welcome : Routes("welcome")
    data object UserRegister : Routes("user_register")
    data object UserLogin : Routes("user_login")
    data object VendorRegister : Routes("vendor_register")
    data object VendorLogin : Routes("vendor_login")
    data object Home : Routes("home")
}