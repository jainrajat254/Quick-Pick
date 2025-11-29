package org.rajat.quickpick.utils.websocket

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import org.rajat.quickpick.QuickPickApplication
import org.rajat.quickpick.utils.Constants
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

actual class WebSocketManager {

    private var stompClient: StompClient? = null
    private val compositeDisposable = CompositeDisposable()
    private val gson = Gson()
    private val context: Context = QuickPickApplication.provideAppContext()

    private var onOrderReceived: ((OrderNotification) -> Unit)? = null
    private var onConnectionChanged: ((Boolean) -> Unit)? = null

    companion object {
        private const val TAG = "WebSocketManager"
        private val WS_URL = Constants.BASE_URL
            .replace("https://", "wss://")
            .replace("http://", "ws://") + "/ws"
    }

    actual fun connect(authToken: String) {
        disconnect()

        val headers = listOf(
            StompHeader("Authorization", "Bearer $authToken")
        )

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL).apply {
            connect(headers)
        }

        val lifecycleDisposable = stompClient?.lifecycle()
            ?.subscribe { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
                        Log.d(TAG, "WebSocket connected")
                        onConnectionChanged?.invoke(true)
                        subscribeToPendingOrders()
                    }
                    LifecycleEvent.Type.CLOSED -> {
                        Log.d(TAG, "WebSocket closed")
                        onConnectionChanged?.invoke(false)
                    }
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(TAG, "WebSocket error", event.exception)
                        onConnectionChanged?.invoke(false)
                    }
                    else -> {}
                }
            }

        lifecycleDisposable?.let { compositeDisposable.add(it) }

        val orderDisposable = stompClient?.topic("/user/queue/orders")
            ?.subscribe { message ->
                try {
                    val notification = gson.fromJson(message.payload, OrderNotification::class.java)
                    Log.d(TAG, "Received order notification: $notification")
                    onOrderReceived?.invoke(notification)
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing order notification", e)
                }
            }

        orderDisposable?.let { compositeDisposable.add(it) }
    }

    private fun subscribeToPendingOrders() {
        val prefs = context.getSharedPreferences("QuickPickPrefs", Context.MODE_PRIVATE)
        val userType = prefs.getString("user_type", "")

        if (userType == "VENDOR") {
            Log.d(TAG, "Vendor connected - backend will send pending orders")
        }
    }

    actual fun disconnect() {
        compositeDisposable.clear()
        stompClient?.disconnect()
        stompClient = null
    }

    actual fun setOrderReceivedListener(listener: (OrderNotification) -> Unit) {
        onOrderReceived = listener
    }

    actual fun setConnectionListener(listener: (Boolean) -> Unit) {
        onConnectionChanged = listener
    }
}
