package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class PasswordOtpRequest(
    val email: String,
    val userType: String
)

