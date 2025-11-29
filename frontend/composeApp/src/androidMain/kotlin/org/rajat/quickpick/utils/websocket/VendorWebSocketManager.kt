package org.rajat.quickpick.utils.websocket

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.rajat.quickpick.QuickPickApplication

@SuppressLint("StaticFieldLeak")
object VendorWebSocketManager {

    private const val TAG = "VendorWebSocketMgr"

    private val context: Context = QuickPickApplication.provideAppContext()
    private var webSocketManager: WebSocketManager? = null

    private val _connectionState = MutableStateFlow(false)
    val connectionState: StateFlow<Boolean> = _connectionState

    private val _newOrderReceived = MutableStateFlow<OrderNotification?>(null)
    val newOrderReceived: StateFlow<OrderNotification?> = _newOrderReceived

    private var isVendorLoggedIn = false


    fun connect() {
        if (webSocketManager != null) {
            Log.d(TAG, "WebSocket already connected")
            return
        }

        val prefs = context.getSharedPreferences("QuickPickPrefs", Context.MODE_PRIVATE)
        val authToken = prefs.getString("auth_token", null)
        val userType = prefs.getString("user_type", null)

        if (authToken == null || userType != "VENDOR") {
            Log.d(TAG, "Not a vendor or no auth token, skipping WebSocket connection")
            return
        }

        Log.d(TAG, "Connecting WebSocket for vendor...")
        isVendorLoggedIn = true

        webSocketManager = WebSocketManager().apply {
            setConnectionListener { isConnected ->
                Log.d(TAG, "WebSocket connection state: $isConnected")
                _connectionState.value = isConnected
            }

            setOrderReceivedListener { notification ->
                Log.d(TAG, "New order received via WebSocket: ${notification.orderId}")
                _newOrderReceived.value = notification
            }

            connect(authToken)
        }
    }


    fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket...")
        webSocketManager?.disconnect()
        webSocketManager = null
        _connectionState.value = false
        isVendorLoggedIn = false
    }


    fun isConnected(): Boolean = _connectionState.value


    fun reconnectIfNeeded() {
        if (isVendorLoggedIn && webSocketManager == null) {
            connect()
        }
    }


    fun clearLastNotification() {
        _newOrderReceived.value = null
    }
}
