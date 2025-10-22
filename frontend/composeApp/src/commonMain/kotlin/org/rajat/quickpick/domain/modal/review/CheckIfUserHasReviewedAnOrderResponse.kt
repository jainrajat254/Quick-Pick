package org.rajat.quickpick.domain.modal.review

import kotlinx.serialization.Serializable

@Serializable
data class CheckIfUserHasReviewedAnOrderResponse(
    val hasReviewed: Boolean? = null
)