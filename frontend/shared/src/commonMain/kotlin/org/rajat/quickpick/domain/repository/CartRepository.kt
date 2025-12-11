package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.cart.AddToCartRequest
import org.rajat.quickpick.domain.modal.cart.CartResponse
import org.rajat.quickpick.domain.modal.cart.ClearCartResponse
import org.rajat.quickpick.domain.modal.cart.UpdateCartItemRequest

interface CartRepository {
    suspend fun addToCart(addToCartRequest: AddToCartRequest): Result<CartResponse>
    suspend fun getCart(): Result<CartResponse>
    suspend fun updateCartItem(menuItemId: String, updateCartItemRequest: UpdateCartItemRequest): Result<CartResponse>
    suspend fun removeFromCart(menuItemId: String): Result<CartResponse>
    suspend fun clearCart(): Result<ClearCartResponse>
}

