package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.cart.AddToCartRequest
import org.rajat.quickpick.domain.modal.cart.CartResponse
import org.rajat.quickpick.domain.modal.cart.ClearCartResponse
import org.rajat.quickpick.domain.modal.cart.UpdateCartItemRequest
import org.rajat.quickpick.domain.repository.CartRepository
import org.rajat.quickpick.utils.UiState

class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartState = MutableStateFlow<UiState<CartResponse>>(UiState.Empty)
    val cartState: StateFlow<UiState<CartResponse>> = _cartState

    private val _addToCartState = MutableStateFlow<UiState<CartResponse>>(UiState.Empty)
    val addToCartState: StateFlow<UiState<CartResponse>> = _addToCartState

    private val _updateCartState = MutableStateFlow<UiState<CartResponse>>(UiState.Empty)
    val updateCartState: StateFlow<UiState<CartResponse>> = _updateCartState

    private val _removeFromCartState = MutableStateFlow<UiState<CartResponse>>(UiState.Empty)
    val removeFromCartState: StateFlow<UiState<CartResponse>> = _removeFromCartState

    private val _clearCartState = MutableStateFlow<UiState<ClearCartResponse>>(UiState.Empty)
    val clearCartState: StateFlow<UiState<ClearCartResponse>> = _clearCartState

    private fun <T> executeWithUiState(
        stateFlow: MutableStateFlow<UiState<T>>,
        block: suspend () -> Result<T>
    ) {
        viewModelScope.launch {
            stateFlow.value = UiState.Loading
            val result = block()
            stateFlow.value = result.fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun addToCart(menuItemId: String, quantity: Int = 1) {
        executeWithUiState(_addToCartState) {
            val request = AddToCartRequest(menuItemId = menuItemId, quantity = quantity)
            val result = cartRepository.addToCart(request)
            result.onSuccess { _cartState.value = UiState.Success(it) }
            result
        }
    }

    fun getCart() {
        executeWithUiState(_cartState) {
            cartRepository.getCart()
        }
    }

    fun updateCartItem(menuItemId: String, quantity: Int) {
        executeWithUiState(_updateCartState) {
            val request = UpdateCartItemRequest(quantity = quantity)
            val result = cartRepository.updateCartItem(menuItemId, request)
            result.onSuccess { _cartState.value = UiState.Success(it) }
            result
        }
    }

    fun removeFromCart(menuItemId: String) {
        executeWithUiState(_removeFromCartState) {
            val result = cartRepository.removeFromCart(menuItemId)
            result.onSuccess { _cartState.value = UiState.Success(it) }
            result
        }
    }

    fun clearCart() {
        executeWithUiState(_clearCartState) {
            val result = cartRepository.clearCart()
            result.onSuccess { _cartState.value = UiState.Empty }
            result
        }
    }

    fun resetCartStates() {
        _cartState.value = UiState.Empty
        _addToCartState.value = UiState.Empty
        _updateCartState.value = UiState.Empty
        _removeFromCartState.value = UiState.Empty
        _clearCartState.value = UiState.Empty
    }
}

