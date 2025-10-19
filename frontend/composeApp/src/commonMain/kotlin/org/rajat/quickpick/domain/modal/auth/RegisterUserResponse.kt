package org.rajat.quickpick.domain.modal.auth

data class RegisterUserResponse(
    val email: String,
    val message: String,
    val name: String,
    val refreshToken: Any,
    val role: String,
    val token: Any,
    val userId: String
)