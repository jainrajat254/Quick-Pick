package org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor

import kotlinx.serialization.Serializable


@Serializable
data class Pageable(
    val offset: Int?=null,
    val pageNumber: Int?=null,
    val pageSize: Int?=null,
    val paged: Boolean?=null,
    val sort: SortX?=null,
    val unpaged: Boolean?=null
)