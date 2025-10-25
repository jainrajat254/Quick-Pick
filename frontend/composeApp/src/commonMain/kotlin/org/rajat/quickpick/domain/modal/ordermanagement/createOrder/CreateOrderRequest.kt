package org.rajat.quickpick.domain.modal.ordermanagement.createOrder

import kotlinx.serialization.Serializable


@Serializable
data class CreateOrderRequest(
    val orderItems: List<OrderItem?>?=null,
    val specialInstructions: String?=null,
    val vendorId: String?=null
)