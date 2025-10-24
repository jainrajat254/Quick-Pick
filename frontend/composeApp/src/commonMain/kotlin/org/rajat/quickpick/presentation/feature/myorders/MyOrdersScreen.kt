package org.rajat.quickpick.presentation.feature.myorders
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dummyproject.data.ordermanagement.getOrderById.GetOrderByIdResponse
import com.example.dummyproject.screens.myorders.components.OrderList
import com.example.dummyproject.screens.myorders.components.StyledTabRow
import com.example.dummyproject.theme.AppTheme



@Composable
fun MyOrderScreen(
    paddingValues: PaddingValues,
    activeOrders: List<GetOrderByIdResponse>,
    completedOrders: List<GetOrderByIdResponse>,
    cancelledOrders: List<GetOrderByIdResponse>,
    isLoading: Boolean,
    onTabSelected: (OrderTab) -> Unit,
    onOrderCancel: (String) -> Unit,
    onOrderRate: (String) -> Unit,
    onOrderAgain: (String) -> Unit,
    onOrderViewDetails: (String) -> Unit,
    onclick: (String) -> Unit
) {
    val tabs = listOf(OrderTab.Active, OrderTab.Completed, OrderTab.Cancelled)
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Tell the parent to load orders when the selected tab changes
    LaunchedEffect(selectedTabIndex) {
        onTabSelected(tabs[selectedTabIndex])
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
                        onOrderCancel = onOrderCancel,
                        onOrderRate = onOrderRate,
                        onOrderAgain = onOrderAgain,
                        onOrderViewDetails = { onOrderViewDetails(it) },
                        onclick={
                            onclick(it)
                        }
                    )

                    is OrderTab.Completed -> OrderList(
                        orders = completedOrders,
                        tabName = "Completed",
                        onOrderCancel = onOrderCancel,
                        onOrderRate = onOrderRate,
                        onOrderAgain = onOrderAgain,
                        onOrderViewDetails = { onOrderViewDetails(it) },
                        onclick={
                            onclick(it)
                        }
                    )

                    is OrderTab.Cancelled -> OrderList(
                        orders = cancelledOrders,
                        tabName = "Cancelled",
                        onOrderCancel = onOrderCancel,
                        onOrderRate = onOrderRate,
                        onOrderAgain = onOrderAgain,
                        onOrderViewDetails = { onOrderViewDetails(it) },
                        onclick={
                            onclick(it)
                        }
                    )
                }
            }
        }
    }
}

//PREVIEW
@Preview(showBackground = true)
@Composable
fun MyOrderScreenPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MyOrderScreen(
                paddingValues = PaddingValues(0.dp),
                activeOrders = emptyList(),
                completedOrders = dummyCompletedOrders,
                cancelledOrders = dummyCancelledOrders,
                isLoading = false,
                onTabSelected = {},
                onOrderCancel = {},
                onOrderRate = {},
                onOrderAgain = {},
                onOrderViewDetails = {},
                onclick = {}
            )
        }
    }
}

