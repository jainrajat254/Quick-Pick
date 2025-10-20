package org.rajat.quickpick.domain.modal.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminCreateRequest(
    val email: String?,
    val fullName: String?,
    val password: String?
)