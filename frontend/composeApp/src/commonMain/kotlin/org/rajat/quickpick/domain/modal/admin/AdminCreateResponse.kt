package org.rajat.quickpick.domain.modal.admin

data class AdminCreateResponse(
    val email: String,
    val message: String,
    val name: String,
    val refreshToken: Any,
    val role: String,
    val token: Any,
    val userId: String
)