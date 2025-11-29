package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.theme.AppTheme
import org.rajat.quickpick.utils.navigation.PendingNavigation

@Composable
@Preview
fun App() {
    AppTheme {
        val navHostController = rememberNavController()

        LaunchedEffect(Unit) {
            if (PendingNavigation.hasPendingNavigation()) {
                val orderId = PendingNavigation.pendingOrderId
                val type = PendingNavigation.pendingNavigationType


                orderId?.let {

                    println("Navigate to order details: $it (type: $type)")

                    PendingNavigation.clear()
                }
            }
        }

        AppNavigation(
            navController = navHostController
        )
    }
}