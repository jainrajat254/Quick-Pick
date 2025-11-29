package org.rajat.quickpick.utils.websocket

import kotlinx.serialization.Serializable

@Serializable
data class OrderNotification(
    val orderId: String,
    val type: String,
    val title: String,
    val message: String,
    val orderStatus: String,
    val totalAmount: Double,
    val timestamp: String
)

expect class WebSocketManager() {
    fun connect(authToken: String)
    fun disconnect()
    fun setOrderReceivedListener(listener: (OrderNotification) -> Unit)
    fun setConnectionListener(listener: (Boolean) -> Unit)
}
