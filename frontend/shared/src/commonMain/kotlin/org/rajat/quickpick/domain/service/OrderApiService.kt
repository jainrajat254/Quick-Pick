package org.rajat.quickpick.domain.service

import org.rajat.quickpick.domain.modal.ordermanagement.CancelOrderResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrderStatsResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrdersResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetVendorOrderByStatusResponse
import org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest
import org.rajat.quickpick.domain.modal.ordermanagement.createOrder.CreateOrderRequest
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getVendorsOrder.GetVendorOrdersResponse

interface OrderApiService {

    suspend fun createOrder(createOrderRequest: CreateOrderRequest): GetOrderByIdResponse

    suspend fun createOrderFromCart(): GetOrderByIdResponse

    suspend fun getOrderById(orderId: String): GetOrderByIdResponse

    suspend fun getMyOrders(): GetMyOrdersResponse

    suspend fun getMyOrdersByStatus(status: String): GetMyOrdersResponse

    suspend fun cancelOrder(orderId: String): CancelOrderResponse

    suspend fun getMyOrderStats(): GetMyOrderStatsResponse


    suspend fun getPendingOrdersForVendor(otp: String? = null): GetMyOrdersResponse

    suspend fun getVendorOrdersPaginated(page: Int = 0, size: Int = 20): GetVendorOrdersResponse

    suspend fun getVendorOrderById(orderId: String): GetOrderByIdResponse

    suspend fun getVendorOrdersByStatus(status: String): GetVendorOrderByStatusResponse

    suspend fun updateOrderStatus(orderId: String, updateOrderStatusRequest: UpdateOrderStateRequest): GetOrderByIdResponse

    suspend fun getVendorOrderStats(): GetMyOrderStatsResponse
}