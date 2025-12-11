package org.rajat.quickpick.domain.modal.ordermanagement.getOrderById

import kotlinx.serialization.Serializable

@Serializable
data class GetOrderByIdResponse(
    val createdAt: String?=null,
    val id: String?=null,
    val orderItems: List<OrderItemX?>?=null,
    val orderStatus: String?=null,
    val specialInstructions: String?=null,
    val storeName: String?=null,
    val totalAmount: Double?=null,
    val updatedAt: String?=null,
    val userId: String?=null,
    val vendorId: String?=null,
    val vendorName: String?=null
)