package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val newPassword: String? = null,
    val token: String? = null,
    val type: String? = null
)