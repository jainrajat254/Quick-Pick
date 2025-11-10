package org.rajat.quickpick.presentation.navigation

sealed class VendorRoutes(val route: String) {
    data object VendorDashboard : VendorRoutes("vendor_dashboard")
    data object VendorOrders : VendorRoutes("vendor_orders")
    data object VendorOrderDetail : VendorRoutes("vendor_order_detail/{orderId}") {
        fun createRoute(orderId: String) = "vendor_order_detail/$orderId"
    }
    data object VendorMenu : VendorRoutes("vendor_menu")
    data object VendorProfile : VendorRoutes("vendor_profile")
}


