package org.rajat.quickpick.data.remote

import org.rajat.quickpick.domain.service.PaymentApiService
import org.rajat.quickpick.domain.service.PaymentInitiateResponse
import org.rajat.quickpick.utils.Constants
import org.rajat.quickpick.utils.network.safeGet
import org.rajat.quickpick.utils.network.safePost
import io.ktor.client.HttpClient
import co.touchlab.kermit.Logger

private val razorLogger = Logger.withTag("RAZORPAYDEBUG")

class PaymentApiServiceImpl(private val httpClient: HttpClient) : PaymentApiService {
    override suspend fun initiatePayment(orderId: String, paymentMethod: String): PaymentInitiateResponse {
        val endpoint = "${Constants.BASE_URL}/api/payments/initiate"
        razorLogger.d { "PaymentApiServiceImpl: initiating payment to $endpoint with orderId=$orderId paymentMethod=$paymentMethod" }
        val resp: PaymentInitiateResponse = httpClient.safePost(
            endpoint = endpoint,
            body = mapOf("orderId" to orderId, "paymentMethod" to paymentMethod)
        )
        razorLogger.d { "PaymentApiServiceImpl: initiatePayment response: $resp" }
        return resp
    }

    override suspend fun getPaymentStatus(orderId: String): PaymentInitiateResponse {
        val endpoint = "${Constants.BASE_URL}/api/payments/status/$orderId"
        razorLogger.d { "PaymentApiServiceImpl: getting payment status from $endpoint" }
        val resp: PaymentInitiateResponse = httpClient.safeGet(
            endpoint = endpoint
        )
        razorLogger.d { "PaymentApiServiceImpl: getPaymentStatus response: $resp" }
        return resp
    }
}
