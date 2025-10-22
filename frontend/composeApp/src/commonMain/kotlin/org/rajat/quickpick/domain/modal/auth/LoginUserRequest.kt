package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserRequest(
    val email: String? = null,
    val password: String? = null
)