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

    private var cachedMyOrders: GetMyOrdersResponse? = null
    private var cachedOrderStats: GetMyOrderStatsResponse? = null
    private var cachedOrderById: MutableMap<String, GetOrderByIdResponse> = mutableMapOf()

    private var myOrdersCacheTime: Long = 0
    private var orderStatsCacheTime: Long = 0
    private val cacheValidityDuration = 60_000L

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
            _orderByIdState.value = UiState.Success(cachedOrderById[orderId]!!)
            return
        }

        executeWithUiState(_orderByIdState) {
            val result = orderRepository.getOrderById(orderId)
            result.onSuccess { order ->
                cachedOrderById[orderId] = order
            }
            result
        }
    }

    fun getMyOrders(forceRefresh: Boolean = false) {
        if (!forceRefresh && cachedMyOrders != null && isCacheValid(myOrdersCacheTime)) {
            _myOrdersState.value = UiState.Success(cachedMyOrders!!)
            return
        }

        executeWithUiState(_myOrdersState) {
            val result = orderRepository.getMyOrders()
            result.onSuccess { orders ->
                cachedMyOrders = orders
                myOrdersCacheTime = Clock.System.now().toEpochMilliseconds()
            }
            result
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
            // Invalidate caches after cancelling
            invalidateOrderCaches()
            result
        }
    }

    fun getMyOrderStats(forceRefresh: Boolean = false) {
        // Use cache if valid and not forcing refresh
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

    /**
     * Fetch ACCEPTED, PREPARING and READY_FOR_PICKUP orders sequentially and merge them.
     * Exposes a UiState with the merged list to avoid UI-level sequential-fetch complexity.
     */
    fun getCombinedAcceptedOrders() {
        viewModelScope.launch {
            _vendorOrdersAcceptedCombinedState.value = UiState.Loading
            try {
                val statuses = listOf("ACCEPTED", "PREPARING", "READY_FOR_PICKUP")
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
                // Always succeed with a (possibly empty) list — UI can decide fallback behavior
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

    fun getVendorOrderStats() {
        executeWithUiState(_vendorOrderStatsState) {
            orderRepository.getVendorOrderStats()
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
        myOrdersCacheTime = 0
        orderStatsCacheTime = 0
    }

    fun invalidateOrderByIdCache(orderId: String) {
        cachedOrderById.remove(orderId)
    }

    fun clearAllCaches() {
        cachedMyOrders = null
        cachedOrderStats = null
        cachedOrderById.clear()
        myOrdersCacheTime = 0
        orderStatsCacheTime = 0
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
