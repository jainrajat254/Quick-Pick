package org.rajat.quickpick.presentation.feature.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.presentation.feature.cart.components.CartItemRow
import org.rajat.quickpick.presentation.feature.cart.components.CartSummary
import org.rajat.quickpick.presentation.feature.cart.components.EmptyCartView




@Composable
fun CartScreen(
    paddingValues: PaddingValues,
    cartItems: List<CartItem>,
    onQuantityChange: (itemId: String, newQuantity: Int) -> Unit,
    onRemoveItem: (itemId: String) -> Unit,
    navController: NavController
) {

    val totalAmount = cartItems.sumOf { it.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        if (cartItems.isEmpty()) {
            EmptyCartView()
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f), // Takes available space
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp) // Padding for list items
            ) {
                items(cartItems, key = { it.id }) { item ->
                    CartItemRow(
                        item = item,
                        onQuantityChange = onQuantityChange,
                        onRemoveItem = onRemoveItem,
                        navController=navController
                    )
                }
            }

            CartSummary(
                totalAmount = totalAmount,
                navController = navController
            )
        }
    }
}








