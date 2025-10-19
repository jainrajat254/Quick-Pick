package org.rajat.quickpick.domain.modal.auth
data class ForgotPasswordRequest(
    val email: String?,
    val userType: String?
)