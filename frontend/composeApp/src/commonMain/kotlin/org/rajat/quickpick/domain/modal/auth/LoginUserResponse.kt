package org.rajat.quickpick.domain.modal.auth

data class LoginUserResponse(
    val email: String??,
    val message: String??,
    val name: String?,
    val refreshToken: String?,
    val token: String?,
    val userId: String?
)