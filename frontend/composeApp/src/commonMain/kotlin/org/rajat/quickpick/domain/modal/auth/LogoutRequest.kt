package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String? = null
)