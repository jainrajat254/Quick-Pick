package org.rajat.quickpick.presentation.feature.vendor.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun VendorOrderDetailScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    orderId: String,
    orderViewModel: OrderViewModel
) {
    val vendorOrderByIdState by orderViewModel.vendorOrderByIdState.collectAsState()
    val updateOrderStatusState by orderViewModel.updateOrderStatusState.collectAsState()

    LaunchedEffect(orderId) {
        orderViewModel.getVendorOrderById(orderId)
    }

    LaunchedEffect(updateOrderStatusState) {
        when (updateOrderStatusState) {
            is UiState.Success -> {
                showToast("Order status updated successfully")
                orderViewModel.getVendorOrderById(orderId)
                orderViewModel.resetUpdateOrderStatusState()
            }
            is UiState.Error -> {
                showToast((updateOrderStatusState as UiState.Error).message ?: "Error updating order")
                orderViewModel.resetUpdateOrderStatusState()
            }
            else -> Unit
        }
    }

    when (vendorOrderByIdState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CustomLoader()
            }
        }

        is UiState.Success -> {
            val order = (vendorOrderByIdState as UiState.Success).data
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Order #${order.id?.takeLast(8) ?: "N/A"}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Status: ${order.orderStatus ?: "Unknown"}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Items",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                order.orderItems?.filterNotNull()?.let { items ->
                    items(items) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.menuItemName ?: "Unknown Item",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Qty: ${item.quantity ?: 0}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "₹${item.totalPrice ?: 0.0}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Subtotal:", style = MaterialTheme.typography.bodyLarge)
                                Text(text = "₹${order.totalAmount ?: 0.0}", style = MaterialTheme.typography.bodyLarge)
                            }
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total:",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "₹${order.totalAmount ?: 0.0}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                item {
                    when (order.orderStatus) {
                        "PENDING" -> {
                            Button(
                                onClick = {
                                    orderViewModel.updateOrderStatus(
                                        orderId,
                                        org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest("ACCEPTED")
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = updateOrderStatusState !is UiState.Loading
                            ) {
                                if (updateOrderStatusState is UiState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Accept Order")
                                }
                            }
                        }
                        "ACCEPTED" -> {
                            Button(
                                onClick = {
                                    orderViewModel.updateOrderStatus(
                                        orderId,
                                        org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest("PREPARING")
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = updateOrderStatusState !is UiState.Loading
                            ) {
                                if (updateOrderStatusState is UiState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Start Preparing")
                                }
                            }
                        }
                        "PREPARING" -> {
                            Button(
                                onClick = {
                                    orderViewModel.updateOrderStatus(
                                        orderId,
                                        org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest("READY_FOR_PICKUP")
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = updateOrderStatusState !is UiState.Loading
                            ) {
                                if (updateOrderStatusState is UiState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Mark as Ready for Pickup")
                                }
                            }
                        }
                        "READY_FOR_PICKUP" -> {
                            Button(
                                onClick = {
                                    orderViewModel.updateOrderStatus(
                                        orderId,
                                        org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest("COMPLETED")
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = updateOrderStatusState !is UiState.Loading
                            ) {
                                if (updateOrderStatusState is UiState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                } else {
                                    Text("Mark as Completed")
                                }
                            }
                        }
                        "COMPLETED" -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Order Completed",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = (vendorOrderByIdState as UiState.Error).message ?: "Error loading order",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { orderViewModel.getVendorOrderById(orderId) }) {
                        Text("Retry")
                    }
                }
            }
        }

        UiState.Empty -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No order found")
            }
        }
    }
}
