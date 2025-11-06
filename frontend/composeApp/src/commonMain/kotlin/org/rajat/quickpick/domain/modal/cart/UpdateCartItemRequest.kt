package org.rajat.quickpick.domain.modal.cart

import kotlinx.serialization.Serializable

@Serializable
data class UpdateCartItemRequest(
    val quantity: Int
)

