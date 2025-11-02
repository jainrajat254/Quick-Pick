package org.rajat.quickpick.presentation.feature.cart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rajat.quickpick.domain.modal.ordermanagement.createOrder.CreateOrderRequest
import org.rajat.quickpick.domain.modal.ordermanagement.createOrder.OrderItem
import org.rajat.quickpick.presentation.feature.cart.CartItem
import org.rajat.quickpick.presentation.navigation.Routes

@Composable
fun CreateOrderBody(
    totalAmount: Double,
    navController: NavHostController,
    specialInstructions: String,
    isLoading: Boolean,
    cartItems: List<CartItem>
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp) // Padding
    ) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total Amount",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                totalAmount.formatTwoDecimals(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val orderItems = cartItems.map { cartItem ->
                    OrderItem(
                        menuItemId = cartItem.id,
                        quantity = cartItem.quantity
                    )
                }

                val request = CreateOrderRequest(
                    orderItems = orderItems,
                    // --- UPDATED: Pass the instructions state ---
                    specialInstructions = specialInstructions.ifEmpty { null },
                    vendorId = "TODO_GET_FROM_VIEWMODEL"
                )

//                    onConfirmOrder(request)
                //viewmodel method to confirm the order
                navController.navigate(Routes.ConfirmOrder.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading && cartItems.isNotEmpty(),
            shape = RoundedCornerShape(50)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Confirm Order", fontWeight = FontWeight.Bold)
            }
        }
    }
}

