package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.data.dummy.dummyCancelledOrders
import org.rajat.quickpick.data.dummy.dummyCompletedOrders
import org.rajat.quickpick.presentation.feature.myorders.MyOrderScreen
import org.rajat.quickpick.presentation.theme.AppTheme


@Preview(showBackground = true)
@Composable
fun MyOrderScreenPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MyOrderScreen(
                activeOrders = emptyList(),
                completedOrders = dummyCompletedOrders,
                cancelledOrders = dummyCancelledOrders,
                isLoading = false,
                navController = rememberNavController(),
                PaddingValues(0.dp)
            )
        }
    }
}

