package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordOtpRequest(
    val email: String,
    val userType: String,
    val otp: String,
    val newPassword: String
)

