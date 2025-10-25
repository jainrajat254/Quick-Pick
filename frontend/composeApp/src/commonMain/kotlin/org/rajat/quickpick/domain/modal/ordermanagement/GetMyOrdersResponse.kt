package org.rajat.quickpick.domain.modal.ordermanagement

import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse

data class GetMyOrdersResponse(
    val count: Int?=null,
    val orders: List<GetOrderByIdResponse?>?=null
)