package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.cart.CartItem
import org.rajat.quickpick.presentation.feature.cart.CartScreen
import org.rajat.quickpick.presentation.theme.AppTheme


// --- PREVIEWS ---
@Preview(showBackground = true, name = "Cart Screen Light")
@Composable
fun CartScreenLightPreview() {
    val sampleItems = remember { mutableStateListOf(
        CartItem("1", "Classic Burger", 12.00, 1, null),
        CartItem("2", "Fries", 4.50, 2, null),
        CartItem("3", "Coke", 2.00, 1, null)
    )}
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CartScreen(
                paddingValues = PaddingValues(0.dp),
                cartItems = sampleItems,
                onQuantityChange = { id, qty ->
                    val index = sampleItems.indexOfFirst { it.id == id }
                    if (index != -1) sampleItems[index] = sampleItems[index].copy(quantity = qty)
                },
                onRemoveItem = { id -> sampleItems.removeAll { it.id == id } },
                navController = rememberNavController()
            )
        }
    }
}



@Preview(showBackground = true, name = "Cart Screen Dark")
@Composable
fun CartScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val sampleItems = remember { mutableStateListOf(
                CartItem("1", "Classic Burger", 12.00, 1, null),
                CartItem("2", "Fries", 4.50, 2, null)
            )}
            CartScreen(
                paddingValues = PaddingValues(bottom = 80.dp),
                cartItems = sampleItems,
                onQuantityChange = { id, qty ->
                    val index = sampleItems.indexOfFirst { it.id == id }
                    if (index != -1) sampleItems[index] = sampleItems[index].copy(quantity = qty)
                },
                onRemoveItem = { id -> sampleItems.removeAll { it.id == id } },
                navController = rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true, name = "Cart Screen Empty")
@Composable
fun CartScreenEmptyPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CartScreen(
                paddingValues = PaddingValues(bottom = 80.dp),
                cartItems = emptyList(),
                onQuantityChange = {_,_ ->},
                onRemoveItem = {},
                navController = rememberNavController()
            )
        }
    }
}
