package org.rajat.quickpick.presentation.feature.cart

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    var quantity: Int,
    val imageUrl: String? = null
)