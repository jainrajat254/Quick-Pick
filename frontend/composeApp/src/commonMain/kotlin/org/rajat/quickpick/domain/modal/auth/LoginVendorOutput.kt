package org.rajat.quickpick.domain.modal.auth

data class LoginVendorOutput(
    val email: String,
    val message: String,
    val name: String,
    val refreshToken: String,
    val role: String,
    val token: String,
    val userId: String
)