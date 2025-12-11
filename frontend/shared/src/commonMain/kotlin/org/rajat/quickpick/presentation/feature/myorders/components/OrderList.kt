package org.rajat.quickpick.presentation.feature.myorders.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse

@Composable
fun OrderList(
    orders: List<GetOrderByIdResponse>,
    tabName: String,
    onOrderCancel: (String) -> Unit,
    onOrderRate: (GetOrderByIdResponse) -> Unit,
    onOrderAgain: (GetOrderByIdResponse) -> Unit,
    onOrderViewDetails: (String) -> Unit,
    onclick: (String) -> Unit
) {
    if (orders.isEmpty()) {
        EmptyOrderCard(selectedTab = tabName)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(orders) { order ->
                OrderItemCard(

                    order = order,
                    onCancel = { order.id?.let(onOrderCancel) ?: Unit },
                    onRate = { onOrderRate(order) },
                    onOrderAgain = { onOrderAgain(order) },
                    onViewDetails = { order.id?.let(onOrderViewDetails) ?: Unit },
                    onClick = {
                        onclick(
                            order.id.toString()
                        )
                    }
                )
            }
        }
    }
}
