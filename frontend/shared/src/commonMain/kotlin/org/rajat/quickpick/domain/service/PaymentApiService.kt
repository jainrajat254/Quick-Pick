package org.rajat.quickpick.domain.service

import kotlinx.serialization.Serializable

@Serializable
data class PaymentInitiateResponse(
    val orderId: String? = null,
    val transactionId: String? = null,
    val paymentMethod: String? = null,
    val paymentStatus: String? = null,
    val amount: Double? = null,
    val paymentUrl: String? = null,
    val razorpayKeyId: String? = null
)

interface PaymentApiService {
    suspend fun initiatePayment(orderId: String, paymentMethod: String = "PAY_NOW"): PaymentInitiateResponse
    suspend fun getPaymentStatus(orderId: String): PaymentInitiateResponse
}
