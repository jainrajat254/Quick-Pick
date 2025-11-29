package org.rajat.quickpick.utils.navigation

object PendingNavigation {
    var pendingOrderId: String? = null
    var pendingNavigationType: String? = null

    fun setPendingOrder(orderId: String, type: String = "NEW_ORDER") {
        pendingOrderId = orderId
        pendingNavigationType = type
    }

    fun clear() {
        pendingOrderId = null
        pendingNavigationType = null
    }

    fun hasPendingNavigation(): Boolean = pendingOrderId != null
}

