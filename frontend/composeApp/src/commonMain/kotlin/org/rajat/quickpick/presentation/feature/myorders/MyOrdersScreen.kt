package org.rajat.quickpick.presentation.feature.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.presentation.feature.myorders.components.OrderList
import org.rajat.quickpick.presentation.feature.myorders.components.OrderTab
import org.rajat.quickpick.presentation.feature.myorders.components.StyledTabRow
import org.rajat.quickpick.presentation.navigation.Routes


@Composable
fun MyOrderScreen(
    activeOrders: List<GetOrderByIdResponse>,
    completedOrders: List<GetOrderByIdResponse>,
    cancelledOrders: List<GetOrderByIdResponse>,
    isLoading: Boolean,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val tabs = listOf(OrderTab.Active, OrderTab.Completed, OrderTab.Cancelled)
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Tell the parent to load orders when the selected tab changes
    LaunchedEffect(selectedTabIndex) {
        tabs[selectedTabIndex]
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
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    when (tabs[selectedTabIndex]) {
                        is OrderTab.Active -> OrderList(
                            orders = activeOrders,
                            tabName = "Active",
                            onOrderCancel = {
                                navController.navigate(Routes.CancelOrder.createRoute(it))
                            },
                            onOrderRate = {
                                navController.navigate(Routes.ReviewOrder.createRoute(it))
                            },
                            onOrderAgain = {  },
                            onOrderViewDetails = {navController.navigate(Routes.OrderDetail.createRoute(it)) },
                            onclick = {
                                navController.navigate(Routes.OrderDetail.createRoute(it))
                            }
                        )

                        is OrderTab.Completed -> OrderList(
                            orders = completedOrders,
                            tabName = "Completed",
                            onOrderCancel = {
                                navController.navigate(Routes.CancelOrder.createRoute(it))
                            },
                            onOrderRate = {
                                navController.navigate(Routes.ReviewOrder.createRoute(it))
                            },
                            onOrderAgain = {  },
                            onOrderViewDetails = {navController.navigate(Routes.OrderDetail.createRoute(it)) },
                            onclick = {
                                navController.navigate(Routes.OrderDetail.createRoute(it))
                            }
                        )

                        is OrderTab.Cancelled -> OrderList(
                            orders = cancelledOrders,
                            tabName = "Cancelled",
                            onOrderCancel = {
                                navController.navigate(Routes.CancelOrder.createRoute(it))
                            },
                            onOrderRate = {
                                navController.navigate(Routes.ReviewOrder.createRoute(it))
                            },
                            onOrderAgain = {},
                            onOrderViewDetails = {navController.navigate(Routes.OrderDetail.createRoute(it)) },
                            onclick = {
                                navController.navigate(Routes.OrderDetail.createRoute(it))
                            }
                        )
                    }
                }
            }
        }

}