package org.rajat.quickpick.presentation.feature.myorders.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.domain.modal.ordermanagement.OrderStatus
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse

@Composable
fun OrderItemCard(
    order: GetOrderByIdResponse,
    onCancel: () -> Unit,
    onRate: () -> Unit,
    onOrderAgain: () -> Unit,
    onViewDetails: () -> Unit,
    onClick : () -> Unit,
    onPayNow: () -> Unit = {},
    paymentUiState: org.rajat.quickpick.domain.service.PaymentInitiateResponse? = null
) {
    val status = try {
        OrderStatus.valueOf(order.orderStatus ?: "PENDING")
    } catch (_: Exception) {
        OrderStatus.PENDING
    }

    val totalItemCount = order.orderItems?.sumOf { it?.quantity ?: 0 } ?: 0
    val title = order.orderItems?.firstOrNull()?.menuItemName ?: order.storeName ?: "Order"

    Card(
        modifier = Modifier.fillMaxWidth()
            .clickable{
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OrderCardHeader(
                title = title,
                totalAmount = order.totalAmount ?: 0.0,
                itemCount = totalItemCount
            )

            Spacer(modifier = Modifier.height(12.dp))

            OrderCardBody(
                createdAt = order.createdAt ?: "",
                status = status
            )

            Spacer(modifier = Modifier.height(16.dp))

            OrderCardFooter(
                status = status,
                paymentStatus = order.paymentStatus,
                onCancel = onCancel,
                onRate = onRate,
                onOrderAgain = onOrderAgain,
                onViewDetails = onViewDetails,
                onPayNow = {
                    if (paymentUiState != null && paymentUiState.orderId == order.id && !paymentUiState.transactionId.isNullOrBlank()) {
                        return@OrderCardFooter
                    }
                    onPayNow()
                }
            )
        }
    }
}