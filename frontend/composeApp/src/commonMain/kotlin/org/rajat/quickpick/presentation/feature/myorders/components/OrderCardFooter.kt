package org.rajat.quickpick.presentation.feature.myorders.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.domain.modal.ordermanagement.OrderStatus

@Composable
fun OrderCardFooter(
    status: OrderStatus,
    onCancel: () -> Unit,
    onRate: () -> Unit,
    onOrderAgain: () -> Unit,
    onViewDetails: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (status) {
            OrderStatus.PENDING -> {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Cancel Order")
                }
                OutlinedButton(
                    onClick = onViewDetails,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Order details")
                }
            }
            OrderStatus.COMPLETED -> {
                OutlinedButton(
                    onClick = onOrderAgain,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Order Again")
                }
                Button(
                    onClick = onRate,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Leave a review")
                }
            }
            OrderStatus.CANCELLED, OrderStatus.REJECTED -> {
                OutlinedButton(
                    onClick = onOrderAgain,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Order Again")
                }
            }
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICKUP -> {
                OutlinedButton(
                    onClick = onViewDetails,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Order details")
                }
            }
        }
    }
}