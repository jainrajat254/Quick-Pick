package org.rajat.quickpick.domain.modal.review.getPaginatedReviewsforVendor

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val comment: String?=null,
    val createdAt: String?=null,
    val id: String?=null,
    val orderId: String?=null,
    val rating: Int?=null,
    val userId: String?=null,
    val userName: String?=null,
    val vendorId: String?=null
)