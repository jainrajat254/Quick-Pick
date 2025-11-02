package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.cart.CartItem
import org.rajat.quickpick.presentation.feature.cart.CheckoutScreen
import org.rajat.quickpick.presentation.theme.AppTheme

//PREVIEWS
@Preview(showBackground = true, name = "Checkout Light")
@Composable
fun CheckoutScreenLightPreview() {
    val sampleItems = listOf(
        CartItem("1", "Classic Burger", 12.00, 1, null),
        CartItem("2", "Fries", 4.50, 2, null)
    )
    val total = sampleItems.sumOf { it.price * it.quantity }
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CheckoutScreen(
                paddingValues = PaddingValues(0.dp),
                cartItems = sampleItems,
                totalAmount = total,
                isLoading = false,
                navController = rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true, name = "Checkout Dark")
@Composable
fun CheckoutScreenDarkPreview() {
    val sampleItems = listOf(
        CartItem("1", "Classic Burger", 12.00, 1, null),
        CartItem("2", "Fries", 4.50, 2, null),
        CartItem("3", "Very Long Item Name That Might Wrap Or Ellipsize", 5.00, 1, null)
    )
    val total = sampleItems.sumOf { it.price * it.quantity }
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CheckoutScreen(
                paddingValues = PaddingValues(bottom = 80.dp),
                cartItems = sampleItems,
                totalAmount = total,
                isLoading = false,
                navController = rememberNavController()
            )
        }
    }
}
