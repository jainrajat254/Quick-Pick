package org.rajat.quickpick.utils.websocket

actual class WebSocketManager {

    actual fun connect(authToken: String, userRole: String) {
        // iOS WebSocket implementation - to be added later
        println("iOS WebSocket connect called with userRole: $userRole")
    }

    actual fun disconnect() {
        // iOS WebSocket implementation - to be added later
    }

    actual fun setOrderReceivedListener(listener: (OrderNotification) -> Unit) {
        // iOS WebSocket implementation - to be added later
    }

    actual fun setConnectionListener(listener: (Boolean) -> Unit) {
        // iOS WebSocket implementation - to be added later
    }
}
