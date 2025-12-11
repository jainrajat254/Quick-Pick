package org.rajat.quickpick.domain.modal.review

import kotlinx.serialization.Serializable

@Serializable
data class GetVendorRatingStatsResponse(
    val averageRating: Double? = null,
    val ratingDistribution: RatingDistributionX? = null,
    val totalReviews: Int? = null,
    val vendorId: String? = null
)