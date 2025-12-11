package org.rajat.quickpick.domain.modal.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminLoginRequest(
    val email: String?,
    val password: String?
)