package org.rajat.quickpick.domain.modal.cart

import kotlinx.serialization.Serializable

@Serializable
data class CartResponse(
    val id: String? = null,
    val userId: String? = null,
    val vendorId: String? = null,
    val vendorName: String? = null,
    val items: List<CartItem> = emptyList(),
    val totalAmount: Double = 0.0,
    val itemCount: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

