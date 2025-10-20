package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginVendorRequest(
    val email: String?,
    val password: String?
)