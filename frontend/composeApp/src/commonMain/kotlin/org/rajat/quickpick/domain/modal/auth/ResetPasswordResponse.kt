package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordResponse(
    val message: String? = null
)