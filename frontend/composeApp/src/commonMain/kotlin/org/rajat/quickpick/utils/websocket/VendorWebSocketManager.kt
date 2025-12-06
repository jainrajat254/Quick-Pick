package org.rajat.quickpick.utils.websocket

import kotlinx.coroutines.flow.StateFlow

expect object VendorWebSocketManager {
    val connectionState: StateFlow<Boolean>
    val newOrderReceived: StateFlow<OrderNotification?>

    fun connect(authToken: String, userRole: String)
    fun disconnect()
    fun isConnected(): Boolean
    fun reconnectIfNeeded(authToken: String, userRole: String)
    fun clearLastNotification()
}

