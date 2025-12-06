package org.rajat.quickpick.utils.websocket

import android.util.Log
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.rajat.quickpick.utils.Constants
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

actual class WebSocketManager {

    private var stompClient: StompClient? = null
    private val compositeDisposable = CompositeDisposable()
    private val gson = Gson()

    private var onOrderReceived: ((OrderNotification) -> Unit)? = null
    private var onConnectionChanged: ((Boolean) -> Unit)? = null
    private var currentUserRole: String = ""
    private var isConnecting = false
    private var isConnected = false

    companion object {
        private const val TAG = "WebSocketManager"
        // SockJS is enabled on backend, so we need to use /ws/websocket suffix
        private val WS_URL = Constants.BASE_URL
            .replace("https://", "wss://")
            .replace("http://", "ws://") + "/ws/websocket"

        init {
            // Set global RxJava error handler to prevent crashes from unhandled errors
            RxJavaPlugins.setErrorHandler { throwable ->
                Log.e(TAG, "RxJava global error handler caught: ${throwable.message}", throwable)
            }
        }
    }

    actual fun connect(authToken: String, userRole: String) {
        if (isConnecting || isConnected) {
            Log.d(TAG, "Already connecting or connected, skipping...")
            return
        }

        disconnect()
        isConnecting = true
        currentUserRole = userRole

        Log.d(TAG, "Connecting to WebSocket: $WS_URL")

        val headers = listOf(
            StompHeader("Authorization", "Bearer $authToken")
        )

        try {
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL).apply {
                withClientHeartbeat(10000)
                withServerHeartbeat(10000)
            }

            // Subscribe to lifecycle events with error handler
            val lifecycleDisposable = stompClient?.lifecycle()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { event ->
                        try {
                            when (event.type) {
                                LifecycleEvent.Type.OPENED -> {
                                    Log.d(TAG, "WebSocket connected")
                                    isConnecting = false
                                    isConnected = true
                                    onConnectionChanged?.invoke(true)
                                    subscribeToOrders()
                                }
                                LifecycleEvent.Type.CLOSED -> {
                                    Log.d(TAG, "WebSocket closed")
                                    isConnecting = false
                                    isConnected = false
                                    onConnectionChanged?.invoke(false)
                                }
                                LifecycleEvent.Type.ERROR -> {
                                    Log.e(TAG, "WebSocket error: ${event.exception?.message}", event.exception)
                                    isConnecting = false
                                    isConnected = false
                                    onConnectionChanged?.invoke(false)
                                }
                                LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> {
                                    Log.w(TAG, "WebSocket server heartbeat failed")
                                }
                                else -> {}
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error handling lifecycle event", e)
                            isConnecting = false
                            isConnected = false
                        }
                    },
                    { error ->
                        Log.e(TAG, "WebSocket lifecycle error", error)
                        isConnecting = false
                        isConnected = false
                        onConnectionChanged?.invoke(false)
                    }
                )

            lifecycleDisposable?.let { compositeDisposable.add(it) }

            // Connect after setting up lifecycle listener
            try {
                stompClient?.connect(headers)
            } catch (e: Exception) {
                Log.e(TAG, "Error calling stompClient.connect()", e)
                isConnecting = false
                isConnected = false
                onConnectionChanged?.invoke(false)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error setting up WebSocket", e)
            isConnecting = false
            isConnected = false
            onConnectionChanged?.invoke(false)
        }
    }

    private fun subscribeToOrders() {
        // Check if we're actually connected before subscribing
        val client = stompClient
        if (!isConnected || client == null) {
            Log.w(TAG, "Cannot subscribe to orders - not connected")
            return
        }

        if (currentUserRole == "VENDOR") {
            Log.d(TAG, "Vendor connected - subscribing to orders")
        }

        try {
            val orderDisposable = client.topic("/user/queue/orders")
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                    { message ->
                        try {
                            val notification = gson.fromJson(message.payload, OrderNotification::class.java)
                            Log.d(TAG, "Received order notification: $notification")
                            onOrderReceived?.invoke(notification)
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing order notification", e)
                        }
                    },
                    { error ->
                        Log.e(TAG, "Error subscribing to orders", error)
                        // Don't crash - just log the error
                    }
                )

            orderDisposable?.let { compositeDisposable.add(it) }
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up order subscription", e)
        }
    }

    actual fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket...")
        isConnecting = false
        isConnected = false
        compositeDisposable.clear()
        try {
            stompClient?.disconnect()
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting WebSocket", e)
        }
        stompClient = null
        currentUserRole = ""
    }

    actual fun setOrderReceivedListener(listener: (OrderNotification) -> Unit) {
        onOrderReceived = listener
    }

    actual fun setConnectionListener(listener: (Boolean) -> Unit) {
        onConnectionChanged = listener
    }
}
