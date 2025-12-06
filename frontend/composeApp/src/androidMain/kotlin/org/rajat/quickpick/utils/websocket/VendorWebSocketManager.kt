package org.rajat.quickpick.utils.websocket

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

actual object VendorWebSocketManager {

    private const val TAG = "VendorWebSocketMgr"

    private var webSocketManager: WebSocketManager? = null

    private val _connectionState = MutableStateFlow(false)
    actual val connectionState: StateFlow<Boolean> = _connectionState

    private val _newOrderReceived = MutableStateFlow<OrderNotification?>(null)
    actual val newOrderReceived: StateFlow<OrderNotification?> = _newOrderReceived

    private var isVendorLoggedIn = false
    private var currentAuthToken: String? = null

    actual fun connect(authToken: String, userRole: String) {
            try {
            if (webSocketManager != null) {
                Log.d(TAG, "WebSocket already connected")
                return
            }

            if (userRole != "VENDOR") {
                Log.d(TAG, "Not a vendor, skipping WebSocket connection")
                return
            }

            Log.d(TAG, "Connecting WebSocket for vendor...")
            isVendorLoggedIn = true
            currentAuthToken = authToken

            webSocketManager = WebSocketManager().apply {
                setConnectionListener { isConnected ->
                    Log.d(TAG, "WebSocket connection state: $isConnected")
                    _connectionState.value = isConnected
                }

                setOrderReceivedListener { notification ->
                    Log.d(TAG, "New order received via WebSocket: ${notification.orderId}")
                    _newOrderReceived.value = notification
                }

                connect(authToken, userRole)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error connecting WebSocket", e)
            _connectionState.value = false
        }
    }

    actual fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket...")
        try {
            webSocketManager?.disconnect()
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting WebSocket", e)
        }
        webSocketManager = null
        _connectionState.value = false
        isVendorLoggedIn = false
        currentAuthToken = null
    }

    actual fun isConnected(): Boolean = _connectionState.value

    actual fun reconnectIfNeeded(authToken: String, userRole: String) {
        try {
            if (isVendorLoggedIn && webSocketManager == null) {
                connect(authToken, userRole)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reconnecting WebSocket", e)
        }
    }

    actual fun clearLastNotification() {
        _newOrderReceived.value = null
    }
}
