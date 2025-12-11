package org.rajat.quickpick.domain.service

import org.rajat.quickpick.domain.modal.cart.AddToCartRequest
import org.rajat.quickpick.domain.modal.cart.CartResponse
import org.rajat.quickpick.domain.modal.cart.ClearCartResponse
import org.rajat.quickpick.domain.modal.cart.UpdateCartItemRequest

interface CartApiService {
    suspend fun addToCart(addToCartRequest: AddToCartRequest): CartResponse
    suspend fun getCart(): CartResponse
    suspend fun updateCartItem(menuItemId: String, updateCartItemRequest: UpdateCartItemRequest): CartResponse
    suspend fun removeFromCart(menuItemId: String): CartResponse
    suspend fun clearCart(): ClearCartResponse
}

