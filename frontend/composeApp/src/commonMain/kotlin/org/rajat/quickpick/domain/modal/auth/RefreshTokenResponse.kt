package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val tokens: Tokens,
    val userId: String,
    val email: String,
    val name: String,
    val role: String,
    val message: String
)