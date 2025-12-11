package org.rajat.quickpick.domain.modal.ordermanagement.getVendorsOrder

import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse


data class GetVendorOrdersResponse(
    val content: List<GetOrderByIdResponse?>?=null,
    val empty: Boolean?=null,
    val first: Boolean?=null,
    val last: Boolean?=null,
    val number: Int?=null,
    val numberOfElements: Int?=null,
    val Pageable: Pageable?=null,
    val size: Int?=null,
    val sort: SortX?=null,
    val totalElements: Int?=null,
    val totalPages: Int?=null
)