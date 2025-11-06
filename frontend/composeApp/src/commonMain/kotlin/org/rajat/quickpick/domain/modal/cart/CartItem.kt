package org.rajat.quickpick.domain.modal.cart

import kotlinx.serialization.Serializable

@Serializable
data class CartItem(
    val menuItemId: String? = null,
    val menuItemName: String? = null,
    val menuItemImage: String? = null,
    val quantity: Int = 0,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val isAvailable: Boolean = true,
    val isVeg: Boolean = false
)

