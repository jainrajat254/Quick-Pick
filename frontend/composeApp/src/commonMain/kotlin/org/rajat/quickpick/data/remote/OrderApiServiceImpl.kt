package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import org.rajat.quickpick.domain.modal.ordermanagement.CancelOrderResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrderStatsResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetMyOrdersResponse
import org.rajat.quickpick.domain.modal.ordermanagement.GetVendorOrderByStatusResponse
import org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest
import org.rajat.quickpick.domain.modal.ordermanagement.createOrder.CreateOrderRequest
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getVendorsOrder.GetVendorOrdersResponse
import org.rajat.quickpick.domain.service.OrderApiService
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safeGet
import org.rajat.quickpick.utils.network.safePatch
import org.rajat.quickpick.utils.network.safePost

class OrderApiServiceImpl(private val httpClient: HttpClient) : OrderApiService {

    override suspend fun createOrder(createOrderRequest: CreateOrderRequest): GetOrderByIdResponse {
        return httpClient.safePost(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.CREATE_ORDER}",
            body = createOrderRequest
        )
    }

    override suspend fun getOrderById(orderId: String): GetOrderByIdResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_ORDER_BY_ID}$orderId"
        )
    }

    override suspend fun getMyOrders(): GetMyOrdersResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_MY_ORDERS}"
        )
    }

    override suspend fun getMyOrdersByStatus(status: String): GetMyOrdersResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_MY_ORDERS_BY_STATUS}$status"
        )
    }

    override suspend fun cancelOrder(orderId: String): CancelOrderResponse {
        return httpClient.safePatch(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.CANCEL_ORDER}$orderId/cancel"
        )
    }

    override suspend fun getMyOrderStats(): GetMyOrderStatsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_MY_ORDER_STATS}"
        )
    }

    override suspend fun getPendingOrdersForVendor(): GetMyOrdersResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_PENDING_ORDERS_VENDOR}"
        )
    }

    override suspend fun getVendorOrdersPaginated(page: Int, size: Int): GetVendorOrdersResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_VENDOR_ORDERS_PAGINATED}",
            queryParams = mapOf(
                "page" to page.toString(),
                "size" to size.toString()
            )
        )
    }

    override suspend fun getVendorOrderById(orderId: String): GetOrderByIdResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_VENDOR_ORDER_BY_ID}$orderId"
        )
    }

    override suspend fun getVendorOrdersByStatus(status: String): GetVendorOrderByStatusResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_VENDOR_ORDERS_BY_STATUS}$status"
        )
    }

    override suspend fun updateOrderStatus(
        orderId: String,
        updateOrderStatusRequest: UpdateOrderStateRequest
    ): GetOrderByIdResponse {
        return httpClient.safePatch(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.UPDATE_ORDER_STATUS}$orderId/status",
            body = updateOrderStatusRequest
        )
    }

    override suspend fun getVendorOrderStats(): GetMyOrderStatsResponse {
        return httpClient.safeGet(
            endpoint = "${Constants.BASE_URL}${Constants.Endpoints.GET_VENDOR_ORDER_STATS}"
        )
    }
}

