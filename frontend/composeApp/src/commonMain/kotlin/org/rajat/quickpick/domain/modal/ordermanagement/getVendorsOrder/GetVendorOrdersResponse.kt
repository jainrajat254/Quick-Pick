package org.rajat.quickpick.domain.modal.ordermanagement.getVendorsOrder

import com.example.dummyproject.data.ordermanagement.getOrderById.GetOrderByIdResponse

data class GetVendorOrdersResponse(
    val content: List<GetOrderByIdResponse>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: SortX,
    val totalElements: Int,
    val totalPages: Int
)