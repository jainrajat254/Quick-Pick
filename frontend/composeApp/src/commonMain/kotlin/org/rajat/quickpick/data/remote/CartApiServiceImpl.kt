package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.cart.AddToCartRequest
import org.rajat.quickpick.domain.modal.cart.CartResponse
import org.rajat.quickpick.domain.modal.cart.ClearCartResponse
import org.rajat.quickpick.domain.modal.cart.UpdateCartItemRequest
import org.rajat.quickpick.domain.service.CartApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safeDelete
import org.rajat.quickpick.utils.network.safeGet
import org.rajat.quickpick.utils.network.safePost
import org.rajat.quickpick.utils.network.safePut

class CartApiServiceImpl(private val httpClient: HttpClient) : CartApiService {

    override suspend fun addToCart(addToCartRequest: AddToCartRequest): CartResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.ADD_TO_CART}",
            body = addToCartRequest
        )
    }

    override suspend fun getCart(): CartResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_CART}"
        )
    }

    override suspend fun updateCartItem(
        menuItemId: String,
        updateCartItemRequest: UpdateCartItemRequest
    ): CartResponse {
        return httpClient.safePut(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_CART_ITEM}$menuItemId",
            body = updateCartItemRequest
        )
    }

    override suspend fun removeFromCart(menuItemId: String): CartResponse {
        return httpClient.safeDelete(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.REMOVE_FROM_CART}$menuItemId"
        )
    }

    override suspend fun clearCart(): ClearCartResponse {
        return httpClient.safeDelete(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.CLEAR_CART}"
        )
    }
}

