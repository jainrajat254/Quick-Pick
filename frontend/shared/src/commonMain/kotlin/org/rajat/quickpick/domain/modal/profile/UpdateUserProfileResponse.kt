package org.rajat.quickpick.domain.modal.profile

import kotlinx.serialization.Serializable


@Serializable
data class UpdateUserProfileResponse(
    val collegeName: String? = null,
    val email: String? = null,
    val emailVerified: Boolean? = null,
    val fullName: String? = null,
    val id: String? = null,
    val phone: String? = null,
    val phoneVerified: Boolean? = null,
    val profileImageUrl: String? = null,
    val role: String? = null
)