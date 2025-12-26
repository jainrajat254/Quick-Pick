package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class IsSessionValidResponse(
    val success: Boolean,
    val statusCode: Int
)

