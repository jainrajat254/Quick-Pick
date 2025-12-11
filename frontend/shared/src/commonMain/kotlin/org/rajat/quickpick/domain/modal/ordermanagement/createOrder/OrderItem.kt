package org.rajat.quickpick.domain.modal.ordermanagement.createOrder
import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val menuItemId: String?=null,
    val quantity: Int?=null
)