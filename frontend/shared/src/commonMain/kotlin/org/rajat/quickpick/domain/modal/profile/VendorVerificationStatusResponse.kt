package org.rajat.quickpick.domain.modal.profile

import kotlinx.serialization.Serializable

@Serializable
data class VendorVerificationStatusResponse(
    val email: String,
    val status: String // "Verified", "Pending", or "Rejected"
)

