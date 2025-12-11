package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class EmailOtpRequest(
    val email: String,
    val userType: String
)
