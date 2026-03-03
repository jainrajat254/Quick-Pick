package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.cart.AddToCartRequest
import org.rajat.quickpick.domain.modal.cart.CartResponse
import org.rajat.quickpick.domain.modal.cart.ClearCartResponse
import org.rajat.quickpick.domain.modal.cart.UpdateCartItemRequest
import org.rajat.quickpick.domain.repository.CartRepository
import org.rajat.quickpick.utils.UiState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CartViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val cacheLogger = Logger.withTag("CartViewModel_Cache")

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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var cachedCart: CartResponse? = null
    private var cartCacheTime: Long = 0
    private val cacheValidityDuration = 60_000L

    init {
        cacheLogger.d { "CartViewModel instance created: ${this.hashCode()}" }
    }

    private fun isCacheValid(cacheTime: Long): Boolean {
        return (Clock.System.now().toEpochMilliseconds() - cacheTime) < cacheValidityDuration
    }

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
            result.onSuccess {
                cachedCart = it
                cartCacheTime = Clock.System.now().toEpochMilliseconds()
                _cartState.value = UiState.Success(it)
            }
            result
        }
    }

    private var isCartLoading = false

    fun getCart(forceRefresh: Boolean = false) {
        cacheLogger.d { "getCart called - forceRefresh=$forceRefresh, cachedData=${cachedCart != null}, cacheValid=${if (cachedCart != null) isCacheValid(cartCacheTime) else false}, currentState=${_cartState.value::class.simpleName}, isLoading=$isCartLoading" }

        if (isCartLoading && !forceRefresh) {
            cacheLogger.d { "⏳ REQUEST IN FLIGHT - Skipping duplicate call" }
            return
        }

        if (!forceRefresh && _cartState.value is UiState.Success && cachedCart != null && isCacheValid(cartCacheTime)) {
            cacheLogger.d { "✅ CACHE HIT - State already Success, returning without API call" }
            return
        }

        if (!forceRefresh && cachedCart != null && isCacheValid(cartCacheTime)) {
            cacheLogger.d { "✅ CACHE HIT - Setting state to cached Success" }
            _cartState.value = UiState.Success(cachedCart!!)
            return
        }

        cacheLogger.d { "❌ CACHE MISS - Making API call" }
        isCartLoading = true
        viewModelScope.launch {
            if (forceRefresh) {
                _isRefreshing.value = true
            } else {
                _cartState.value = UiState.Loading
            }

            val result = cartRepository.getCart()
            result.fold(
                onSuccess = { data ->
                    cachedCart = data
                    cartCacheTime = Clock.System.now().toEpochMilliseconds()
                    _cartState.value = UiState.Success(data)
                    cacheLogger.d { "API call successful, data cached" }
                },
                onFailure = { error ->
                    _cartState.value = UiState.Error(error.message ?: "Unknown error")
                    cacheLogger.e { "API call failed: ${error.message}" }
                }
            )
            _isRefreshing.value = false
            isCartLoading = false
        }
    }

    fun updateCartItem(menuItemId: String, quantity: Int) {
        executeWithUiState(_updateCartState) {
            val request = UpdateCartItemRequest(quantity = quantity)
            val result = cartRepository.updateCartItem(menuItemId, request)
            result.onSuccess {
                cachedCart = it
                cartCacheTime = Clock.System.now().toEpochMilliseconds()
                _cartState.value = UiState.Success(it)
            }
            result
        }
    }

    fun removeFromCart(menuItemId: String) {
        executeWithUiState(_removeFromCartState) {
            val result = cartRepository.removeFromCart(menuItemId)
            result.onSuccess {
                cachedCart = it
                cartCacheTime = Clock.System.now().toEpochMilliseconds()
                _cartState.value = UiState.Success(it)
            }
            result
        }
    }

    fun clearCart() {
        executeWithUiState(_clearCartState) {
            val result = cartRepository.clearCart()
            result.onSuccess {
                cachedCart = null
                cartCacheTime = 0
                _cartState.value = UiState.Empty
            }
            result
        }
    }

    fun invalidateCache() {
        cachedCart = null
        cartCacheTime = 0
    }

    fun resetCartStates() {
        _cartState.value = UiState.Empty
        _addToCartState.value = UiState.Empty
        _updateCartState.value = UiState.Empty
        _removeFromCartState.value = UiState.Empty
        _clearCartState.value = UiState.Empty
        cachedCart = null
        cartCacheTime = 0
    }
}

