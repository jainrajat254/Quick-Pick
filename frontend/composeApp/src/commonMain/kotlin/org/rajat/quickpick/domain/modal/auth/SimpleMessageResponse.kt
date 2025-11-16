package org.rajat.quickpick.domain.modal.auth

import kotlinx.serialization.Serializable

@Serializable
data class SimpleMessageResponse(
    val message: String? = null
)
