package org.rajat.quickpick.domain.modal.ordermanagement

import kotlinx.serialization.Serializable
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
@Serializable
data class GetMyOrdersResponse(
    val count: Int?=null,
    val orders: List<GetOrderByIdResponse?>?=null
)