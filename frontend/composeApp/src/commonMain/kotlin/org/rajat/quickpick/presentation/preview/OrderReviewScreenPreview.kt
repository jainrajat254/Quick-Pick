package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.myorders.OrderReviewScreen
import org.rajat.quickpick.presentation.theme.AppTheme

// --- PREVIEWS ---
@Preview(showBackground = true, name = "Order Review Light")
@Composable
fun OrderReviewScreenLightPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OrderReviewScreen(
                orderId = "QKPK12345",
                itemName = "Paneer Curry",
                itemImageUrl = null,
                navController = rememberNavController(),
                PaddingValues(0.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Order Review Dark")
@Composable
fun OrderReviewScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            OrderReviewScreen(
                orderId = "QKPK12345",
                itemName = "Paneer Curry",
                itemImageUrl = null,
                navController = rememberNavController(),
                PaddingValues(0.dp)
            )
        }
    }
}