package org.rajat.quickpick.domain.repository

import org.rajat.quickpick.domain.modal.ordermanagement.CancelOrderResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrderStatsResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrdersResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetVendorOrderByStatusResponse
import org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest
import org.rajat.quickpick.domain.modal.ordermanagement.createOrder.CreateOrderRequest
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getVendorsOrder.GetVendorOrdersResponse

interface OrderRepository {

    // Student Order Operations
    suspend fun createOrder(createOrderRequest: CreateOrderRequest): Result<GetOrderByIdResponse>

    suspend fun createOrderFromCart(): Result<GetOrderByIdResponse>

    suspend fun getOrderById(orderId: String): Result<GetOrderByIdResponse>

    suspend fun getMyOrders(): Result<GetMyOrdersResponse>

    suspend fun getMyOrdersByStatus(status: String): Result<GetMyOrdersResponse>

    suspend fun cancelOrder(orderId: String): Result<CancelOrderResponse>

    suspend fun getMyOrderStats(): Result<GetMyOrderStatsResponse>

    // Vendor Order Operations
    suspend fun getPendingOrdersForVendor(otp: String? = null): Result<GetMyOrdersResponse>

    suspend fun getVendorOrdersPaginated(page: Int = 0, size: Int = 20): Result<GetVendorOrdersResponse>

    suspend fun getVendorOrderById(orderId: String): Result<GetOrderByIdResponse>

    suspend fun getVendorOrdersByStatus(status: String): Result<GetVendorOrderByStatusResponse>

    suspend fun updateOrderStatus(orderId: String, updateOrderStatusRequest: UpdateOrderStateRequest): Result<GetOrderByIdResponse>

    suspend fun getVendorOrderStats(): Result<GetMyOrderStatsResponse>
}
