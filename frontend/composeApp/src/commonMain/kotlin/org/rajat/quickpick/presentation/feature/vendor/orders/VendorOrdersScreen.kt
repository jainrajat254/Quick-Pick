package org.rajat.quickpick.presentation.feature.vendor.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.feature.vendor.orders.components.VendorOrderCard
import org.rajat.quickpick.presentation.navigation.VendorRoutes
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.UiState

@Composable
fun VendorOrdersScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    orderViewModel: OrderViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedAcceptedSubTab by remember { mutableStateOf(0) }
    val tabs = listOf("Pending", "Accepted", "Completed")
    val acceptedSubTabs = listOf("Preparing", "Ready")

    val vendorOrdersByStatusState by orderViewModel.vendorOrdersByStatusState.collectAsState()

    LaunchedEffect(selectedTab, selectedAcceptedSubTab) {
        val status = when (selectedTab) {
            0 -> "PENDING"
            1 -> {
                when (selectedAcceptedSubTab) {
                    0 -> "PREPARING"
                    1 -> "READY_FOR_PICKUP"
                    else -> "PREPARING"
                }
            }
            2 -> "COMPLETED"
            else -> "PENDING"
        }
        orderViewModel.getVendorOrdersByStatus(status)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        if (index == 1) {
                            selectedAcceptedSubTab = 0
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        if (selectedTab == 1) {
            SecondaryTabRow(
                selectedTabIndex = selectedAcceptedSubTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                acceptedSubTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedAcceptedSubTab == index,
                        onClick = { selectedAcceptedSubTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedAcceptedSubTab == index) FontWeight.SemiBold else FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    )
                }
            }
        }

        when (vendorOrdersByStatusState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CustomLoader()
                }
            }

            is UiState.Success -> {
                val orders = (vendorOrdersByStatusState as UiState.Success).data.orders?.filterNotNull() ?: emptyList()

                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val emptyMessage = when (selectedTab) {
                            0 -> "No pending orders"
                            1 -> "No ${acceptedSubTabs[selectedAcceptedSubTab].lowercase()} orders"
                            2 -> "No completed orders"
                            else -> "No orders"
                        }
                        Text(
                            text = emptyMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(orders.size) { index ->
                            val order = orders[index]
                            VendorOrderCard(
                                order = order,
                                onClick = {
                                    order.id?.let { orderId ->
                                        navController.navigate(VendorRoutes.VendorOrderDetail.createRoute(orderId))
                                    }
                                }
                            )
                        }
                    }
                }
            }

            is UiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = (vendorOrdersByStatusState as UiState.Error).message ?: "Error loading orders",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = {
                                val status = when (selectedTab) {
                                    0 -> "PENDING"
                                    1 -> {
                                        when (selectedAcceptedSubTab) {
                                            0 -> "PREPARING"
                                            1 -> "READY_FOR_PICKUP"
                                            else -> "PREPARING"
                                        }
                                    }
                                    2 -> "COMPLETED"
                                    else -> "PENDING"
                                }
                                orderViewModel.getVendorOrdersByStatus(status)
                            }
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }

            UiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No orders yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
