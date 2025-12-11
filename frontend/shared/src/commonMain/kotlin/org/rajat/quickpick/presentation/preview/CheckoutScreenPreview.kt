package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.cart.CheckoutScreen
import org.rajat.quickpick.presentation.theme.AppTheme

//PREVIEWS
@Preview(showBackground = true, name = "Checkout Light")
@Composable
fun CheckoutScreenLightPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CheckoutScreen(
                paddingValues = PaddingValues(0.dp),
                navController = rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true, name = "Checkout Dark")
@Composable
fun CheckoutScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            CheckoutScreen(
                paddingValues = PaddingValues(bottom = 80.dp),
                navController = rememberNavController()
            )
        }
    }
}
