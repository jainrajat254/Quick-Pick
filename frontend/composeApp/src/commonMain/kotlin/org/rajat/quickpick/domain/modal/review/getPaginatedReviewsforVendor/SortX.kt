package org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor

import kotlinx.serialization.Serializable


@Serializable
data class SortX(
    val empty: Boolean? = null,
    val sorted: Boolean? = null,
    val unsorted: Boolean? = null
)