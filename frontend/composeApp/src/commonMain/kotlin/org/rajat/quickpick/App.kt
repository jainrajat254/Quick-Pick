package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import co.touchlab.kermit.Logger
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.theme.AppTheme
import org.rajat.quickpick.utils.navigation.PendingNavigation

@Composable
@Preview
fun App() {
    val logger = Logger.withTag("ThemeLogs")
    val dataStore: LocalDataStore = koinInject()

    val userRole by dataStore.getUserRoleFlow().collectAsState(initial = null)
    val isVendor = userRole.equals("VENDOR", ignoreCase = true)

    LaunchedEffect(userRole) {
        logger.d { "User role changed: $userRole" }
        logger.d { "isVendor: $isVendor" }
    }

    logger.d { "Rendering AppTheme with isVendor: $isVendor, userRole: $userRole" }

    AppTheme(isVendor = isVendor) {
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