package org.rajat.quickpick.presentation.feature.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.feature.myorders.components.OrderList
import org.rajat.quickpick.presentation.feature.myorders.components.OrderTab
import org.rajat.quickpick.presentation.feature.myorders.components.StyledTabRow
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.CartViewModel
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast


@Composable
fun MyOrderScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    orderViewModel: OrderViewModel = koinInject(),
    cartViewModel: CartViewModel = koinInject()
) {
    val tabs = listOf(OrderTab.Active, OrderTab.Completed, OrderTab.Cancelled)
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val myOrdersState by orderViewModel.myOrdersState.collectAsState()

    LaunchedEffect(Unit) {
        orderViewModel.getMyOrders()
    }

    LaunchedEffect(myOrdersState) {
        if (myOrdersState is UiState.Error) {
            showToast((myOrdersState as UiState.Error).message)
        }
    }

    val allOrders = when (myOrdersState) {
        is UiState.Success -> (myOrdersState as UiState.Success).data.orders?.filterNotNull() ?: emptyList()
        else -> emptyList()
    }

    val activeOrders = allOrders.filter {
        it.orderStatus in listOf("PENDING", "ACCEPTED", "PREPARING", "READY")
    }

    val completedOrders = allOrders.filter {
        it.orderStatus == "COMPLETED"
    }

    val cancelledOrders = allOrders.filter {
        it.orderStatus == "CANCELLED"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StyledTabRow(
            tabs = tabs.map { it.title },
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            when (val state = myOrdersState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is UiState.Success -> {
                    val currentOrders = when (tabs[selectedTabIndex]) {
                        is OrderTab.Active -> activeOrders
                        is OrderTab.Completed -> completedOrders
                        is OrderTab.Cancelled -> cancelledOrders
                        else -> emptyList()
                    }

                    OrderList(
                        orders = currentOrders,
                        tabName = tabs[selectedTabIndex].title,
                        onOrderCancel = {
                            navController.navigate(
                                AppScreenUser.CancelOrder(orderId = it)
                            )
                        },
                        onOrderRate = {order->
                            val orderId = order.id
                            val firstItem = order.orderItems?.firstOrNull()
                            val itemName = firstItem?.menuItemName ?: "Item"
                            val itemImageUrl =  ""

                            if (orderId != null) {
                                navController.navigate(
                                    AppScreenUser.ReviewOrder(
                                        orderId = orderId,
                                        itemName = itemName,
                                        itemImageUrl = itemImageUrl
                                    )
                                )
                            } else {
                                showToast("Error: Cannot review this order.")
                            }
                        },
                        onOrderAgain = { order ->
                            cartViewModel.clearCart()
                            order.orderItems
                                ?.filterNotNull()
                                ?.forEach { item ->

                                    val id = item.menuItemId ?: return@forEach
                                    val qty = item.quantity ?: return@forEach

                                    cartViewModel.addToCart(
                                        menuItemId = id,
                                        quantity = qty
                                    )
                                }

                            navController.navigate(AppScreenUser.Cart)
                        },
                        onOrderViewDetails = {
                            navController.navigate(AppScreenUser.OrderDetail(
                                orderId = it
                            ))
                        },
                        onclick = {
                            navController.navigate(AppScreenUser.OrderDetail(
                                orderId = it
                            )
                            )
                        }
                    )
                }
                is UiState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 32.dp)
                    ) {
                        Text(
                            text = "Failed to load orders",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = state.message ?: "Unknown error occurred",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                is UiState.Empty -> {
                    Text(
                        text = "No orders yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }
        }
    }
}