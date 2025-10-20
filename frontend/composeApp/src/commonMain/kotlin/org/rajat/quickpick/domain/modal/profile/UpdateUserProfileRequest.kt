package org.rajat.quickpick.domain.modal.profile


import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    val department: String?=null,
    val fullName: String?=null,
    val phone: String?=null,
    val profileImageUrl: String?=null
)