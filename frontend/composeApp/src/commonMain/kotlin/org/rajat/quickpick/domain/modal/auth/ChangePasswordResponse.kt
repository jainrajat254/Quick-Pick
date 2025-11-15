package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordResponse(
    val message: String? = null
)

