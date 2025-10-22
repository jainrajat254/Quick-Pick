package org.rajat.quickpick.domain.modal.review

import kotlinx.serialization.Serializable

@Serializable
data class GetReviewsByVendorSpecificRatingResponse(
    val averageRating: Double? = null,
    val ratingDistribution: RatingDistribution? = null,
    val totalReviews: Int? = null,
    val vendorId: String? = null
)