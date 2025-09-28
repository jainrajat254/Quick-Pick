package org.rajat.quickpick.presentation.navigation

sealed class Routes(val route: String) {

    data object Splash : Routes("splash")
    data object Welcome : Routes("welcome")
    data object GetOtpLogin : Routes("get_otp_login")
    data object VerifyOtpLogin : Routes("verify_otp_login")
    data object GetOtpRegister : Routes("get_otp_register")
    data object VerifyOtpRegister : Routes("verify_otp_register")
    data object Logout : Routes("logout")


    // Main Screens
    data object Home : Routes("home")
}