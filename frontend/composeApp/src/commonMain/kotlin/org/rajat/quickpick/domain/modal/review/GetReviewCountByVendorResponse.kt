package org.rajat.quickpick.domain.modal.review

import kotlinx.serialization.Serializable

@Serializable
data class GetReviewCountByVendorResponse(
    val count: Int?=null
)