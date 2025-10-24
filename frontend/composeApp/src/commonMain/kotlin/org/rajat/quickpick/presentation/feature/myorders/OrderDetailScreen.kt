package org.rajat.quickpick.presentation.feature.myorders
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dummyproject.data.ordermanagement.OrderStatus
import com.example.dummyproject.data.ordermanagement.getOrderById.GetOrderByIdResponse
import com.example.dummyproject.data.ordermanagement.getOrderById.OrderItemX
import com.example.dummyproject.screens.myorders.components.OrderDetailHeader
import com.example.dummyproject.screens.myorders.components.StatusChip
import com.example.dummyproject.screens.myorders.components.formatOrderDate
import com.example.dummyproject.theme.AppColors
import com.example.dummyproject.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    paddingValues: PaddingValues,
    order: GetOrderByIdResponse?,
    isLoading: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        } else if (order == null) {
            Text(
                "Order details not found.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            val status = try {
                OrderStatus.valueOf(order.orderStatus ?: "PENDING")
            } catch (e: Exception) {
                OrderStatus.PENDING
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start
            ) {
                OrderDetailHeader(
                    storeName = order.storeName ?: "Store",
                    orderId = order.id ?: "N/A"
                )

                Spacer(modifier = Modifier.height(16.dp))

                StatusChip(status = status)

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Items Ordered",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                order.orderItems?.forEach { item ->
                    if (item != null) {
                        OrderItemRow(item = item)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(16.dp))

                //Special Instructions
                if (!order.specialInstructions.isNullOrBlank()) {
                    Text(
                        "Special Instructions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        order.specialInstructions,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                //Total Amount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total Amount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "$${"%.2f".format(order.totalAmount ?: 0.0)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary // Green for total
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Ordered on: ${formatOrderDate(order.createdAt ?: "")}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp)) // Bottom padding
            }
        }
    }
}



@Composable
private fun OrderItemRow(item: OrderItemX) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "${item.quantity ?: 0} x",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(40.dp)
        )
        Text(
            item.menuItemName ?: "Unknown Item",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            "$${"%.2f".format(item.totalPrice ?: 0.0)}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End
        )
    }
}

// --- PREVIEWS ---

@Preview(showBackground = true, name = "Order Details Light")
@Composable
fun OrderDetailScreenLightPreview() {
    val order = GetOrderByIdResponse(
        id = "QKPK12345",
        storeName = "Campus Burger Joint",
        totalAmount = 16.50,
        createdAt = "2025-10-23T14:30:00Z",
        orderStatus = "ACCEPTED",
        orderItems = listOf(
            OrderItemX(menuItemName = "Classic Burger", quantity = 1, totalPrice = 12.00),
            OrderItemX(menuItemName = "Fries", quantity = 1, totalPrice = 4.50)
        ),
        vendorName = "Vendor A",
        specialInstructions = "No pickles, extra ketchup please."
    )
    AppTheme(darkTheme = false) {
        Surface(Modifier.fillMaxSize()){
            OrderDetailScreen(
                paddingValues = PaddingValues(0.dp),
                order = order,
                isLoading = false,
            )
        }
    }
}

@Preview(showBackground = true, name = "Order Details Dark")
@Composable
fun OrderDetailScreenDarkPreview() {
    val order = GetOrderByIdResponse(
        id = "QKPK67890",
        storeName = "Sweet Treats",
        totalAmount = 8.50,
        createdAt = "2025-10-19T11:00:00Z",
        orderStatus = "PENDING",
        orderItems = listOf(
            OrderItemX(menuItemName = "Strawberry Cheesecake", quantity = 1, totalPrice = 8.50)
        ),
        vendorName = "Vendor B",
        specialInstructions = null
    )
    AppTheme(darkTheme = true) {
        Surface(Modifier.fillMaxSize()){
            OrderDetailScreen(
                paddingValues = PaddingValues(0.dp),
                order = order,
                isLoading = false,
            )
        }
    }
}

@Preview(showBackground = true, name = "Order Details Loading")
@Composable
fun OrderDetailScreenLoadingPreview() {
    AppTheme(darkTheme = true) {
        Surface(Modifier.fillMaxSize()){
            OrderDetailScreen(
                paddingValues = PaddingValues(0.dp),
                order = null,
                isLoading = true,
            )
        }
    }
}

