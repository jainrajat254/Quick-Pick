package org.rajat.quickpick.domain.modal.admin

import kotlinx.serialization.Serializable
import org.rajat.quickpick.domain.modal.auth.Tokens

@Serializable
data class AdminLoginResponse(
    val tokens: Tokens,
    val userId: String,
    val email: String,
    val name: String,
    val role: String,
    val message: String
)