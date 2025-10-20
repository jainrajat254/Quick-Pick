package org.rajat.quickpick.domain.modal.review

import kotlinx.serialization.Serializable

@Serializable
data class CreateReviewStudentRequest(
    val comment: String?=null,
    val orderId: String?=null,
    val rating: Int?=null,
    val vendorId: String?=null
)