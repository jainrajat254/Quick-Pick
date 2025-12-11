package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class Tokens(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,           // in seconds
    val tokenType: String          // "Bearer"
)

