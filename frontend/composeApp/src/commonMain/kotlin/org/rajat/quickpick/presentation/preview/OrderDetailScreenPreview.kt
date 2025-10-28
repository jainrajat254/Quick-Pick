package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.OrderItemX
import org.rajat.quickpick.presentation.feature.myorders.OrderDetailScreen
import org.rajat.quickpick.presentation.theme.AppTheme


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
                order = order,
                isLoading = false,
                navController = rememberNavController(),
                PaddingValues(0.dp)
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
                order = order,
                isLoading = false,
                navController = rememberNavController(),
                PaddingValues(0.dp)
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
                order = null,
                isLoading = true,
                navController = rememberNavController(),
                PaddingValues(0.dp)
            )
        }
    }
}

