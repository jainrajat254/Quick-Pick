package org.rajat.quickpick.domain.modal.ordermanagement.getVendorsOrder
data class Pageable(
    val offset: Int?=null,
    val pageNumber: Int?=null,
    val pageSize: Int?=null,
    val paged: Boolean?=null,
    val sort: SortX?=null,
    val unpaged: Boolean?=null
)