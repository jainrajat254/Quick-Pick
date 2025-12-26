package org.rajat.quickpick.presentation.feature.myorders
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.ordermanagement.OrderStatus
import org.rajat.quickpick.presentation.feature.myorders.components.OrderDetailFields
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    navController: NavHostController,
    paddingValues: PaddingValues,
    orderViewModel: OrderViewModel = koinInject()
) {
    val orderByIdState by orderViewModel.orderByIdState.collectAsState()

    // Fetch order details when screen loads
    LaunchedEffect(orderId) {
        orderViewModel.getOrderById(orderId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = orderByIdState) {
            is UiState.Loading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            is UiState.Success -> {
                val order = state.data
                val status = try {
                    OrderStatus.valueOf(order.orderStatus ?: "PENDING")
                } catch (e: Exception) {
                    OrderStatus.PENDING
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.Start
                ) {
                    OrderDetailFields(order, status)
                }
            }
            is UiState.Error -> {
                Text(
                    text = "Failed to load order details",
                    color = MaterialTheme.colorScheme.error
                )
            }
            is UiState.Empty -> {
                Text(
                    "Order details not found.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
