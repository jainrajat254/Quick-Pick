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
import co.touchlab.kermit.Logger

private val razorLogger = Logger.withTag("RAZORPAYDEBUG")

@Composable
fun OrderCardFooter(
    status: OrderStatus,
    paymentStatus: String? = null,
    onCancel: () -> Unit,
    onRate: () -> Unit,
    onOrderAgain: () -> Unit,
    onViewDetails: () -> Unit,
    onPayNow: () -> Unit = {}
) {
    val isPaid = paymentStatus == "PAID"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        when (status) {
            OrderStatus.PENDING -> {
                Button(
                    onClick = {
                        razorLogger.d { "Pay button area: Cancel clicked (PENDING)" }
                        onCancel()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text("Cancel")
                }
                OutlinedButton(
                    onClick = {
                        razorLogger.d { "Pay button area: View details clicked (PENDING)" }
                        onViewDetails()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Order details")
                }
            }
            OrderStatus.COMPLETED -> {
                OutlinedButton(
                    onClick = {
                        razorLogger.d { "Pay button area: Order Again clicked (COMPLETED)" }
                        onOrderAgain()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Order Again")
                }
                Button(
                    onClick = {
                        razorLogger.d { "Pay button area: Rate clicked (COMPLETED)" }
                        onRate()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Leave a review")
                }
            }
            OrderStatus.CANCELLED, OrderStatus.REJECTED -> {
                OutlinedButton(
                    onClick = {
                        razorLogger.d { "Pay button area: Order Again clicked (CANCELLED/REJECTED)" }
                        onOrderAgain()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Order Again")
                }
            }
            OrderStatus.ACCEPTED -> {
                if (isPaid) {
                    // Payment already done -> show only Order details
                    OutlinedButton(
                        onClick = {
                            razorLogger.d { "Pay button area: View details clicked (PAID)" }
                            onViewDetails()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Order details")
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            razorLogger.d { "Pay button area: View details clicked (ACCEPTED)" }
                            onViewDetails()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Order details")
                    }
                    Button(
                        onClick = {
                            razorLogger.d { "Pay button area: Pay Now clicked (ACCEPTED)" }
                            onPayNow()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Pay Now")
                    }
                }
            }
            OrderStatus.PREPARING,
            OrderStatus.READY_FOR_PICKUP -> {
                if (isPaid) {
                    // If paid, show single Order details button
                    OutlinedButton(
                        onClick = {
                            razorLogger.d { "Pay button area: View details clicked (PAID)" }
                            onViewDetails()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Order details")
                    }
                } else {
                    OutlinedButton(
                        onClick = {
                            razorLogger.d { "Pay button area: View details clicked (PREPARING/READY)" }
                            onViewDetails()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Order details")
                    }
                    Button(
                        onClick = {
                            razorLogger.d { "Pay button area: Pay Now clicked (PREPARING/READY)" }
                            onPayNow()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Pay Now")
                    }
                }
            }
        }
    }
}