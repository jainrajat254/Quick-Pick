package org.rajat.quickpick.domain.modal.ordermanagement
import kotlinx.serialization.Serializable
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.OrderItemX

@Serializable
data class GetVendorOrderByIdResponse(
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