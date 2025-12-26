package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val collegeName: String? = null,
    val email: String? = null,
    val fullName: String? = null,
    val password: String? = null,
    val phone: String? = null
)