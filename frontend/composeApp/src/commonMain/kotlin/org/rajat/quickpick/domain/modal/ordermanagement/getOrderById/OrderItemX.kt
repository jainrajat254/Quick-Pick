package org.rajat.quickpick.domain.modal.ordermanagement.getOrderById
import kotlinx.serialization.Serializable

@Serializable
data class OrderItemX(
    val menuItemId: String?=null,
    val menuItemName: String?=null,
    val quantity: Int?=null,
    val totalPrice: Double?=null,
    val unitPrice: Double?=null
)