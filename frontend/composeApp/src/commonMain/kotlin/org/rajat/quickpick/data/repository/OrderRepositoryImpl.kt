package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.modal.ordermanagement.CancelOrderResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrderStatsResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrdersResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetVendorOrderByStatusResponse
import org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest
import org.rajat.quickpick.domain.modal.ordermanagement.createOrder.CreateOrderRequest
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getVendorsOrder.GetVendorOrdersResponse
import org.rajat.quickpick.domain.repository.OrderRepository
import org.rajat.quickpick.domain.service.OrderApiService

class OrderRepositoryImpl(
    private val orderApiService: OrderApiService
) : OrderRepository {

    override suspend fun createOrder(createOrderRequest: CreateOrderRequest): Result<GetOrderByIdResponse> {
        return try {
            Result.success(orderApiService.createOrder(createOrderRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createOrderFromCart(): Result<GetOrderByIdResponse> {
        return try {
            Result.success(orderApiService.createOrderFromCart())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrderById(orderId: String): Result<GetOrderByIdResponse> {
        return try {
            Result.success(orderApiService.getOrderById(orderId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyOrders(): Result<GetMyOrdersResponse> {
        return try {
            Result.success(orderApiService.getMyOrders())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyOrdersByStatus(status: String): Result<GetMyOrdersResponse> {
        return try {
            Result.success(orderApiService.getMyOrdersByStatus(status))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cancelOrder(orderId: String): Result<CancelOrderResponse> {
        return try {
            Result.success(orderApiService.cancelOrder(orderId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyOrderStats(): Result<GetMyOrderStatsResponse> {
        return try {
            Result.success(orderApiService.getMyOrderStats())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Vendor Order Operations
    override suspend fun getPendingOrdersForVendor(): Result<GetMyOrdersResponse> {
        return try {
            Result.success(orderApiService.getPendingOrdersForVendor())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVendorOrdersPaginated(page: Int, size: Int): Result<GetVendorOrdersResponse> {
        return try {
            Result.success(orderApiService.getVendorOrdersPaginated(page, size))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVendorOrderById(orderId: String): Result<GetOrderByIdResponse> {
        return try {
            Result.success(orderApiService.getVendorOrderById(orderId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVendorOrdersByStatus(status: String): Result<GetVendorOrderByStatusResponse> {
        return try {
            Result.success(orderApiService.getVendorOrdersByStatus(status))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateOrderStatus(
        orderId: String,
        updateOrderStatusRequest: UpdateOrderStateRequest
    ): Result<GetOrderByIdResponse> {
        return try {
            Result.success(orderApiService.updateOrderStatus(orderId, updateOrderStatusRequest))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVendorOrderStats(): Result<GetMyOrderStatsResponse> {
        return try {
            Result.success(orderApiService.getVendorOrderStats())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
