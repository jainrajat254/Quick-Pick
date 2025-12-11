package org.rajat.quickpick.domain.modal.ordermanagement

import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse

data class GetMyOrderByStatusResponse(
    val count: Int?=null,
    val orders: List<GetOrderByIdResponse?>?=null
)