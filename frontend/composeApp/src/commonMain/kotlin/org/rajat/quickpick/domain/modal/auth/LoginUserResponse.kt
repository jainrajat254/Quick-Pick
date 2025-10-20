package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginUserResponse(
    val email: String?=null,
    val message: String?=null,
    val name: String?=null,
    val refreshToken: String?=null,
    val token: String?=null,
    val userId: String?=null
)