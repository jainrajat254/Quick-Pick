package org.rajat.quickpick.domain.modal.cart

import kotlinx.serialization.Serializable

@Serializable
data class AddToCartRequest(
    val menuItemId: String,
    val quantity: Int = 1
)

