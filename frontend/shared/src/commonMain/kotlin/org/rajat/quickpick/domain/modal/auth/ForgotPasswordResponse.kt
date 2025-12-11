package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponse(
    val message: String? = null
)