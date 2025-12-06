package org.rajat.quickpick.utils.websocket

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual object VendorWebSocketManager {

    private val _connectionState = MutableStateFlow(false)
    actual val connectionState: StateFlow<Boolean> = _connectionState

    private val _newOrderReceived = MutableStateFlow<OrderNotification?>(null)
    actual val newOrderReceived: StateFlow<OrderNotification?> = _newOrderReceived

    actual fun connect(authToken: String, userRole: String) {
        println("iOS VendorWebSocketManager connect called")
    }

    actual fun disconnect() {
        println("iOS VendorWebSocketManager disconnect called")
    }

    actual fun isConnected(): Boolean = _connectionState.value

    actual fun reconnectIfNeeded(authToken: String, userRole: String) {
        println("iOS VendorWebSocketManager reconnectIfNeeded called")
    }

    actual fun clearLastNotification() {
        _newOrderReceived.value = null
    }
}

