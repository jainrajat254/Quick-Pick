package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginVendorResponse(
    val email: String?,
    val message: String?,
    val name: String?,
    val refreshToken: String?,
    val role: String?,
    val token: String?,
    val userId: String?
)