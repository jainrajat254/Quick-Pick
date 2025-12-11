package org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor

import kotlinx.serialization.Serializable


@Serializable
data class GetPaginatedReviewsForVendorResponse(
    val content: List<Content?>? = null,
    val empty: Boolean? = null,
    val first: Boolean? = null,
    val last: Boolean? = null,
    val number: Int? = null,
    val numberOfElements: Int? = null,
    val pageable: Pageable? = null,
    val size: Int? = null,
    val sort: SortX? = null,
    val totalElements: Int? = null,
    val totalPages: Int? = null
)