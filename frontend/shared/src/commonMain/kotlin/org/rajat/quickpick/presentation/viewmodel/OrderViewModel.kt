package org.rajat.quickpick.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.ordermanagement.CancelOrderResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrderStatsResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrdersResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetVendorOrderByStatusResponse
import org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest
import org.rajat.quickpick.domain.modal.ordermanagement.createOrder.CreateOrderRequest
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getVendorsOrder.GetVendorOrdersResponse
import org.rajat.quickpick.domain.repository.OrderRepository
import org.rajat.quickpick.domain.service.PaymentApiService
import org.rajat.quickpick.domain.service.PaymentInitiateResponse
import org.rajat.quickpick.utils.UiState
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private val razorLogger = Logger.withTag("RAZORPAYDEBUG")

@OptIn(ExperimentalTime::class)
class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val paymentApiService: PaymentApiService
) : ViewModel() {

    private val _createOrderState = MutableStateFlow<UiState<GetOrderByIdResponse>>(UiState.Empty)
    val createOrderState: StateFlow<UiState<GetOrderByIdResponse>> = _createOrderState

    private val _orderByIdState = MutableStateFlow<UiState<GetOrderByIdResponse>>(UiState.Empty)
    val orderByIdState: StateFlow<UiState<GetOrderByIdResponse>> = _orderByIdState

    private val _myOrdersState = MutableStateFlow<UiState<GetMyOrdersResponse>>(UiState.Empty)
    val myOrdersState: StateFlow<UiState<GetMyOrdersResponse>> = _myOrdersState

    private val _myOrdersByStatusState = MutableStateFlow<UiState<GetMyOrdersResponse>>(UiState.Empty)
    val myOrdersByStatusState: StateFlow<UiState<GetMyOrdersResponse>> = _myOrdersByStatusState

    private val _cancelOrderState = MutableStateFlow<UiState<CancelOrderResponse>>(UiState.Empty)
    val cancelOrderState: StateFlow<UiState<CancelOrderResponse>> = _cancelOrderState

    private val _myOrderStatsState = MutableStateFlow<UiState<GetMyOrderStatsResponse>>(UiState.Empty)
    val myOrderStatsState: StateFlow<UiState<GetMyOrderStatsResponse>> = _myOrderStatsState

    private val _pendingOrdersState = MutableStateFlow<UiState<GetMyOrdersResponse>>(UiState.Empty)
    val pendingOrdersState: StateFlow<UiState<GetMyOrdersResponse>> = _pendingOrdersState

    private val _vendorOrdersPaginatedState = MutableStateFlow<UiState<GetVendorOrdersResponse>>(UiState.Empty)
    val vendorOrdersPaginatedState: StateFlow<UiState<GetVendorOrdersResponse>> = _vendorOrdersPaginatedState

    private val _vendorOrderByIdState = MutableStateFlow<UiState<GetOrderByIdResponse>>(UiState.Empty)
    val vendorOrderByIdState: StateFlow<UiState<GetOrderByIdResponse>> = _vendorOrderByIdState

    private val _vendorOrdersByStatusState = MutableStateFlow<UiState<GetVendorOrderByStatusResponse>>(UiState.Empty)
    val vendorOrdersByStatusState: StateFlow<UiState<GetVendorOrderByStatusResponse>> = _vendorOrdersByStatusState

    private val _vendorOrdersAcceptedCombinedState = MutableStateFlow<UiState<List<GetOrderByIdResponse>>>(UiState.Empty)
    val vendorOrdersAcceptedCombinedState: StateFlow<UiState<List<GetOrderByIdResponse>>> = _vendorOrdersAcceptedCombinedState

    private val _updateOrderStatusState = MutableStateFlow<UiState<GetOrderByIdResponse>>(UiState.Empty)
    val updateOrderStatusState: StateFlow<UiState<GetOrderByIdResponse>> = _updateOrderStatusState

    private val _vendorOrderStatsState = MutableStateFlow<UiState<GetMyOrderStatsResponse>>(UiState.Empty)
    val vendorOrderStatsState: StateFlow<UiState<GetMyOrderStatsResponse>> = _vendorOrderStatsState

    private val _paymentUiState = MutableStateFlow<PaymentInitiateResponse?>(null)
    val paymentUiState: StateFlow<PaymentInitiateResponse?> = _paymentUiState

    private val _paymentErrorState = MutableStateFlow<String?>(null)
    val paymentErrorState: StateFlow<String?> = _paymentErrorState

    private val _paymentSuccessEvent = MutableStateFlow<String?>(null)
    val paymentSuccessEvent: StateFlow<String?> = _paymentSuccessEvent

    private val _initialVendorOrdersTab = MutableStateFlow<Int?>(null)
    val initialVendorOrdersTab: StateFlow<Int?> = _initialVendorOrdersTab

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _currentOrderId = MutableStateFlow<String?>(null)
    val currentOrderId: StateFlow<String?> = _currentOrderId

    fun setInitialVendorOrdersTab(tab: Int) {
        _initialVendorOrdersTab.value = tab
    }

    fun resetInitialVendorOrdersTab() {
        _initialVendorOrdersTab.value = null
    }

    private var cachedMyOrders: GetMyOrdersResponse? = null
    private var cachedOrderStats: GetMyOrderStatsResponse? = null
    private var cachedOrderById: MutableMap<String, GetOrderByIdResponse> = mutableMapOf()
    private var cachedVendorOrderStats: GetMyOrderStatsResponse? = null

    private var myOrdersCacheTime: Long = 0
    private var orderStatsCacheTime: Long = 0
    private var vendorOrderStatsCacheTime: Long = 0
    private val cacheValidityDuration = 60_000L

    private val orderLogger = Logger.withTag("OrderViewModel_Cache")

    init {
        orderLogger.d { "OrderViewModel instance created" }
        getMyOrders(forceRefresh = false)
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

    @OptIn(ExperimentalTime::class)
    private fun isCacheValid(cacheTime: Long): Boolean {
        return (Clock.System.now().toEpochMilliseconds() - cacheTime) < cacheValidityDuration
    }

    fun createOrder(createOrderRequest: CreateOrderRequest) {
        executeWithUiState(_createOrderState) {
            val result = orderRepository.createOrder(createOrderRequest)
            invalidateOrderCaches()
            result
        }
    }

    fun createOrderFromCart() {
        executeWithUiState(_createOrderState) {
            val result = orderRepository.createOrderFromCart()
            invalidateOrderCaches()
            result
        }
    }

    fun getOrderById(orderId: String, forceRefresh: Boolean = false) {
        if (!forceRefresh && cachedOrderById.containsKey(orderId)) {
            if (_orderByIdState.value is UiState.Success && _currentOrderId.value == orderId) {
                return
            }
            _currentOrderId.value = orderId
            _orderByIdState.value = UiState.Success(cachedOrderById[orderId]!!)
            return
        }

        viewModelScope.launch {
            _orderByIdState.value = UiState.Loading
            val result = orderRepository.getOrderById(orderId)
            result.fold(
                onSuccess = { order ->
                    cachedOrderById[orderId] = order
                    _currentOrderId.value = orderId
                    _orderByIdState.value = UiState.Success(order)
                },
                onFailure = { error ->
                    _orderByIdState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun isOrderDataLoadedFor(orderId: String): Boolean {
        return _orderByIdState.value is UiState.Success && _currentOrderId.value == orderId
    }

    private var isMyOrdersLoading = false

    fun getMyOrders(forceRefresh: Boolean = false) {
        orderLogger.d { "getMyOrders called - forceRefresh=$forceRefresh, isLoading=$isMyOrdersLoading, cachedData=${cachedMyOrders != null}, currentState=${_myOrdersState.value::class.simpleName}" }

        if (isMyOrdersLoading && !forceRefresh) {
            orderLogger.d { "⏳ REQUEST IN FLIGHT - Skipping duplicate call" }
            return
        }

        if (!forceRefresh && _myOrdersState.value is UiState.Success && cachedMyOrders != null && isCacheValid(myOrdersCacheTime)) {
            orderLogger.d { "✅ CACHE HIT - State already Success, returning without API call" }
            return
        }

        if (!forceRefresh && cachedMyOrders != null && isCacheValid(myOrdersCacheTime)) {
            orderLogger.d { "✅ CACHE HIT - Setting state to cached Success" }
            _myOrdersState.value = UiState.Success(cachedMyOrders!!)
            return
        }

        orderLogger.d { "❌ CACHE MISS - Making API call" }
        isMyOrdersLoading = true
        viewModelScope.launch {
            if (forceRefresh) {
                _isRefreshing.value = true
            } else {
                _myOrdersState.value = UiState.Loading
            }

            val result = orderRepository.getMyOrders()
            result.fold(
                onSuccess = { orders ->
                    cachedMyOrders = orders
                    myOrdersCacheTime = Clock.System.now().toEpochMilliseconds()
                    _myOrdersState.value = UiState.Success(orders)
                    orderLogger.d { "API call successful, data cached" }
                },
                onFailure = { error ->
                    _myOrdersState.value = UiState.Error(error.message ?: "Unknown error")
                    orderLogger.e { "API call failed: ${error.message}" }
                }
            )
            _isRefreshing.value = false
            isMyOrdersLoading = false
        }
    }

    fun getMyOrdersByStatus(status: String) {
        executeWithUiState(_myOrdersByStatusState) {
            orderRepository.getMyOrdersByStatus(status)
        }
    }

    fun cancelOrder(orderId: String) {
        executeWithUiState(_cancelOrderState) {
            val result = orderRepository.cancelOrder(orderId)
            invalidateOrderCaches()
            result
        }
    }

    fun getMyOrderStats(forceRefresh: Boolean = false) {
        if (!forceRefresh && cachedOrderStats != null && isCacheValid(orderStatsCacheTime)) {
            _myOrderStatsState.value = UiState.Success(cachedOrderStats!!)
            return
        }

        executeWithUiState(_myOrderStatsState) {
            val result = orderRepository.getMyOrderStats()
            result.onSuccess { stats ->
                cachedOrderStats = stats
                orderStatsCacheTime = Clock.System.now().toEpochMilliseconds()
            }
            result
        }
    }

    // Vendor Order Methods
    fun getPendingOrdersForVendor() {
        executeWithUiState(_pendingOrdersState) {
            orderRepository.getPendingOrdersForVendor()
        }
    }

    fun getPendingOrdersForVendor(otp: String?) {
        executeWithUiState(_pendingOrdersState) {
            orderRepository.getPendingOrdersForVendor(otp)
        }
    }

    fun getVendorOrdersPaginated(page: Int = 0, size: Int = 20) {
        executeWithUiState(_vendorOrdersPaginatedState) {
            orderRepository.getVendorOrdersPaginated(page, size)
        }
    }

    fun getVendorOrderById(orderId: String) {
        executeWithUiState(_vendorOrderByIdState) {
            orderRepository.getVendorOrderById(orderId)
        }
    }

    fun getVendorOrdersByStatus(status: String) {
        executeWithUiState(_vendorOrdersByStatusState) {
            orderRepository.getVendorOrdersByStatus(status)
        }
    }

   
    fun getCombinedAcceptedOrders() {
        viewModelScope.launch {
            _vendorOrdersAcceptedCombinedState.value = UiState.Loading
            try {
                val statuses = listOf("ACCEPTED", "PREPARING", "PACKED", "READY_FOR_PICKUP")
                val combined = mutableListOf<GetOrderByIdResponse>()
                var anySuccess = false
                for (s in statuses) {
                    val result = orderRepository.getVendorOrdersByStatus(s)
                    result.onSuccess { resp ->
                        val list = resp.orders?.filterNotNull() ?: emptyList()
                        if (list.isNotEmpty()) {
                            combined += list
                            anySuccess = true
                        }
                    }
                }
                _vendorOrdersAcceptedCombinedState.value = UiState.Success(combined.distinctBy { it.id })
            } catch (e: Exception) {
                _vendorOrdersAcceptedCombinedState.value = UiState.Error(e.message ?: "Error fetching combined orders")
            }
        }
    }

    fun updateOrderStatus(orderId: String, updateOrderStatusRequest: UpdateOrderStateRequest) {
        executeWithUiState(_updateOrderStatusState) {
            orderRepository.updateOrderStatus(orderId, updateOrderStatusRequest)
        }
    }

    fun getVendorOrderStats(forceRefresh: Boolean = false) {
        if (!forceRefresh && cachedVendorOrderStats != null && isCacheValid(vendorOrderStatsCacheTime)) {
            if (_vendorOrderStatsState.value is UiState.Success) {
                return
            }
            _vendorOrderStatsState.value = UiState.Success(cachedVendorOrderStats!!)
            return
        }

        viewModelScope.launch {
            if (forceRefresh) {
                _isRefreshing.value = true
            } else {
                _vendorOrderStatsState.value = UiState.Loading
            }

            val result = orderRepository.getVendorOrderStats()
            result.fold(
                onSuccess = { stats ->
                    cachedVendorOrderStats = stats
                    vendorOrderStatsCacheTime = Clock.System.now().toEpochMilliseconds()
                    _vendorOrderStatsState.value = UiState.Success(stats)
                },
                onFailure = { error ->
                    _vendorOrderStatsState.value = UiState.Error(error.message ?: "Unknown error")
                }
            )
            _isRefreshing.value = false
        }
    }

    fun initiatePayment(orderId: String, paymentMethod: String = "PAY_NOW") {
        viewModelScope.launch {
            _paymentUiState.value = null
            _paymentErrorState.value = null
            razorLogger.d { "OrderViewModel: initiatePayment called for orderId=$orderId" }
            try {
                razorLogger.d { "OrderViewModel: calling paymentApiService.initiatePayment($orderId, $paymentMethod)" }
                val resp = paymentApiService.initiatePayment(orderId, paymentMethod)
                razorLogger.d { "OrderViewModel: initiatePayment response: $resp" }
                _paymentUiState.value = resp
                // Start polling payment status automatically on successful initiation
                // This avoids polling even when initiation failed.
                if (!resp.transactionId.isNullOrBlank()) {
                    pollPaymentStatus(orderId)
                }
            } catch (e: Exception) {
                razorLogger.d { "OrderViewModel: initiatePayment exception: ${e.message}" }
                _paymentErrorState.value = e.message ?: "Error initiating payment"
            }
        }
    }

    fun pollPaymentStatus(orderId: String, timeoutMs: Long = 45000L, intervalMs: Long = 2000L) {
        viewModelScope.launch {
            razorLogger.d { "OrderViewModel: pollPaymentStatus started for orderId=$orderId timeout=$timeoutMs interval=$intervalMs" }
            val start = Clock.System.now().toEpochMilliseconds()
            while (Clock.System.now().toEpochMilliseconds() - start < timeoutMs) {
                try {
                    razorLogger.d { "OrderViewModel: polling payment status for orderId=$orderId" }
                    val statusResp = paymentApiService.getPaymentStatus(orderId)
                    razorLogger.d { "OrderViewModel: poll response: $statusResp" }
                    if (statusResp.paymentStatus == "PAID") {
                        razorLogger.d { "OrderViewModel: payment status PAID for orderId=$orderId" }
                        getMyOrders(forceRefresh = true)
                        // emit success event for UI to show OTP dialog
                        _paymentSuccessEvent.value = orderId
                        _paymentUiState.value = null
                        return@launch
                    }
                } catch (e: Exception) {
                    razorLogger.d { "OrderViewModel: poll exception for orderId=$orderId: ${e.message}" }
                }
                delay(intervalMs)
            }
            razorLogger.d { "OrderViewModel: pollPaymentStatus timed out for orderId=$orderId" }
        }
    }

    fun invalidateOrderCaches() {
        cachedMyOrders = null
        cachedOrderStats = null
        cachedVendorOrderStats = null
        myOrdersCacheTime = 0
        orderStatsCacheTime = 0
        vendorOrderStatsCacheTime = 0
    }

    fun invalidateOrderByIdCache(orderId: String) {
        cachedOrderById.remove(orderId)
    }

    fun invalidateVendorOrderStatsCache() {
        cachedVendorOrderStats = null
        vendorOrderStatsCacheTime = 0
    }

    fun clearAllCaches() {
        cachedMyOrders = null
        cachedOrderStats = null
        cachedVendorOrderStats = null
        cachedOrderById.clear()
        myOrdersCacheTime = 0
        orderStatsCacheTime = 0
        vendorOrderStatsCacheTime = 0
    }

    fun resetCreateOrderState() {
        _createOrderState.value = UiState.Empty
    }

    fun resetCancelOrderState() {
        _cancelOrderState.value = UiState.Empty
    }

    fun resetUpdateOrderStatusState() {
        _updateOrderStatusState.value = UiState.Empty
    }

    fun resetAllStates() {
        _createOrderState.value = UiState.Empty
        _orderByIdState.value = UiState.Empty
        _myOrdersState.value = UiState.Empty
        _myOrdersByStatusState.value = UiState.Empty
        _cancelOrderState.value = UiState.Empty
        _myOrderStatsState.value = UiState.Empty
        _pendingOrdersState.value = UiState.Empty
        _vendorOrdersPaginatedState.value = UiState.Empty
        _vendorOrderByIdState.value = UiState.Empty
        _vendorOrdersByStatusState.value = UiState.Empty
        _updateOrderStatusState.value = UiState.Empty
        _vendorOrderStatsState.value = UiState.Empty
    }

    fun resetPaymentErrorState() {
        _paymentErrorState.value = null
    }

    fun resetPaymentSuccessEvent() {
        _paymentSuccessEvent.value = null
    }
}
