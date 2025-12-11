package org.rajat.quickpick.domain.modal.cart

import kotlinx.serialization.Serializable

@Serializable
data class ClearCartResponse(
    val message: String? = null
)

