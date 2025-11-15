package org.rajat.quickpick.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class AppScreenVendor {
    @Serializable
    data object VendorDashboard : AppScreenVendor()

    @Serializable
    data object VendorOrders : AppScreenVendor()

    @Serializable
    data class VendorOrderDetail(val orderId: String) : AppScreenVendor()

    @Serializable
    data object VendorMenu : AppScreenVendor()

    @Serializable
    data object VendorProfile : AppScreenVendor()

    @Serializable
    data object VendorProfileUpdate : AppScreenVendor()

    @Serializable
    data object AddMenuItemScreen : AppScreenVendor()

    @Serializable
    data object HelpAndSupportScreenVendor : AppScreenVendor()

    @Serializable
    data class UpdateMenuItemScreen(val orderId : String) : AppScreenVendor()

    @Serializable
    data class VendorReviewsScreen(val vendorId: String) : AppScreenVendor()

}
fun getAppScreenVendorFromRoute(route: String?): AppScreenVendor?? {
    if (route == null) return null

    val screenMap = mapOf(
        "VendorDashboard" to AppScreenVendor.VendorDashboard,
        "VendorOrders" to AppScreenVendor.VendorOrders,
        "VendorMenu" to AppScreenVendor.VendorMenu,
        "VendorProfile" to AppScreenVendor.VendorProfile,
        "VendorOrderDetail" to AppScreenVendor.VendorOrderDetail(""),
        "VendorProfileUpdate" to AppScreenVendor.VendorProfileUpdate,
        "AddMenuItemScreen" to AppScreenVendor.AddMenuItemScreen,
        "HelpAndSupportScreenVendor" to AppScreenVendor.HelpAndSupportScreenVendor,
        "UpdateMenuItemScreen" to AppScreenVendor.UpdateMenuItemScreen(
            orderId = ""
        ),
        "VendorReviewsScreen" to AppScreenVendor.VendorReviewsScreen(
            vendorId = ""
        ),
        )

    return screenMap.entries.firstOrNull { route.contains(it.key) }?.value
}