package org.rajat.quickpick.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppScreenUser {
    @Serializable
    data object Splash : AppScreenUser()

    @Serializable
    data object Welcome : AppScreenUser()

    @Serializable
    data object LaunchWelcome : AppScreenUser()

    @Serializable
    data object Onboarding1 : AppScreenUser()

    @Serializable
    data object Onboarding2 : AppScreenUser()

    @Serializable
    data object Onboarding3 : AppScreenUser()

    @Serializable
    data object UserRegister : AppScreenUser()

    @Serializable
    data object UserLogin : AppScreenUser()

    @Serializable
    data object VendorRegister : AppScreenUser()

    @Serializable
    data object VendorLogin : AppScreenUser()

    @Serializable
    data class EmailOtpVerify(val email: String, val userType: String) : AppScreenUser()

    @Serializable
    data object HomeScreen : AppScreenUser()

    @Serializable
    data class VendorDetail(val vendorId: String) : AppScreenUser()

    @Serializable
    data class MenuItemCategory(val vendorId: String, val category: String) : AppScreenUser()

    @Serializable
    data object Orders : AppScreenUser()

    @Serializable
    data object Profile : AppScreenUser()

    @Serializable
    data class OrderDetail(val orderId: String) : AppScreenUser()

    @Serializable
    data class CancelOrder(val orderId: String) : AppScreenUser()

    @Serializable
    data class ReviewOrder(
        val orderId: String,
        val itemName: String,
        val itemImageUrl: String
    ) : AppScreenUser()

    @Serializable
    data object CancelOrderConfirmation : AppScreenUser()

    @Serializable
    data object ReviewOrderConfirmation : AppScreenUser()

    @Serializable
    data object MyProfile : AppScreenUser()

    @Serializable
    data object ContactUs : AppScreenUser()

    @Serializable
    data object ChangePassword : AppScreenUser()

    @Serializable
    data object NotificationSetting : AppScreenUser()

    @Serializable
    data object Settings : AppScreenUser()

    @Serializable
    data object Cart : AppScreenUser()

    @Serializable
    data object Checkout : AppScreenUser()

    // Changed to data class to hold the orderId
    @Serializable
    data class ConfirmOrder(val orderId: String) : AppScreenUser()

    @Serializable
    data object HelpAndFaqs : AppScreenUser()

    @Serializable
    data class ForgotPassword(val userType: String) : AppScreenUser()

    @Serializable
    data class ResetPasswordOtp(val email: String, val userType: String) : AppScreenUser()
}

fun getAppScreenUserFromRoute(route: String?): AppScreenUser? {
    if (route == null) return null

    val screenMap = mapOf(
        "Splash" to AppScreenUser.Splash,
        "Welcome" to AppScreenUser.Welcome,
        "LaunchWelcome" to AppScreenUser.LaunchWelcome,
        "Onboarding1" to AppScreenUser.Onboarding1,
        "Onboarding2" to AppScreenUser.Onboarding2,
        "Onboarding3" to AppScreenUser.Onboarding3,
        "UserRegister" to AppScreenUser.UserRegister,
        "UserLogin" to AppScreenUser.UserLogin,
        "VendorRegister" to AppScreenUser.VendorRegister,
        "VendorLogin" to AppScreenUser.VendorLogin,
        "EmailOtpVerify" to AppScreenUser.EmailOtpVerify(email = "", userType = ""),
        "HomeScreen" to AppScreenUser.HomeScreen,
        "VendorDetail" to AppScreenUser.VendorDetail(vendorId = ""),
        "MenuItemCategory" to AppScreenUser.MenuItemCategory(vendorId = "", category = ""),
        "Orders" to AppScreenUser.Orders,
        "OrderDetail" to AppScreenUser.OrderDetail(
            orderId = ""
        ),
        "CancelOrder" to AppScreenUser.CancelOrder(
            orderId = ""
        ),
        "ReviewOrder" to AppScreenUser.ReviewOrder(
            orderId = "",
            itemName = "",
            itemImageUrl = ""
        ),
        "CancelOrderConfirmation" to AppScreenUser.CancelOrderConfirmation,
        "ReviewOrderConfirmation" to AppScreenUser.ReviewOrderConfirmation,
        "Profile" to AppScreenUser.Profile,
        "MyProfile" to AppScreenUser.MyProfile,
        "ContactUs" to AppScreenUser.ContactUs,
        "ChangePassword" to AppScreenUser.ChangePassword,
        "NotificationSetting" to AppScreenUser.NotificationSetting,
        "Settings" to AppScreenUser.Settings,
        "Cart" to AppScreenUser.Cart,
        "Checkout" to AppScreenUser.Checkout,
        "ConfirmOrder" to AppScreenUser.ConfirmOrder(
            orderId = ""
        ),
        "HelpAndFaqs" to AppScreenUser.HelpAndFaqs,
        "ForgotPassword" to AppScreenUser.ForgotPassword(userType = ""),
        "ResetPasswordOtp" to AppScreenUser.ResetPasswordOtp(email = "", userType = ""),
    )

    return screenMap.entries.firstOrNull { route.contains(it.key) }?.value
}