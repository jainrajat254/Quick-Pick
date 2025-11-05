package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.modal.cart.AddToCartRequest
import org.rajat.quickpick.domain.modal.cart.CartResponse
import org.rajat.quickpick.domain.modal.cart.ClearCartResponse
import org.rajat.quickpick.domain.modal.cart.UpdateCartItemRequest
import org.rajat.quickpick.domain.repository.CartRepository
import org.rajat.quickpick.domain.service.CartApiService

class CartRepositoryImpl(private val cartApiService: CartApiService) : CartRepository {

    override suspend fun addToCart(addToCartRequest: AddToCartRequest): Result<CartResponse> {
        return try {
            val response = cartApiService.addToCart(addToCartRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCart(): Result<CartResponse> {
        return try {
            val response = cartApiService.getCart()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCartItem(
        menuItemId: String,
        updateCartItemRequest: UpdateCartItemRequest
    ): Result<CartResponse> {
        return try {
            val response = cartApiService.updateCartItem(menuItemId, updateCartItemRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeFromCart(menuItemId: String): Result<CartResponse> {
        return try {
            val response = cartApiService.removeFromCart(menuItemId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearCart(): Result<ClearCartResponse> {
        return try {
            val response = cartApiService.clearCart()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

