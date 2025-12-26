package org.rajat.quickpick.presentation.feature.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.feature.myorders.components.ReasonRow
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.ErrorUtils
import co.touchlab.kermit.Logger

private val logger = Logger.withTag("CancelOrderScreen")

@Composable
fun CancelOrderScreen(
    orderId: String,
    navController: NavHostController,
    paddingValues: PaddingValues,
    orderViewModel: OrderViewModel = koinInject()
) {
    val reasons = listOf(
        "Order placed by mistake",
        "Item(s) unavailable",
        "Changed my mind",
        "Delivery time too long"
    )
    var selectedReason by remember { mutableStateOf<String?>(null) }
    var otherReasonText by remember { mutableStateOf("") }
    val isOtherSelected = selectedReason == "Others"

    var showConfirmDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val cancelOrderState by orderViewModel.cancelOrderState.collectAsState()

    LaunchedEffect(cancelOrderState) {
        when (cancelOrderState) {
            is UiState.Success -> {
                orderViewModel.resetCancelOrderState()
                navController.navigate(AppScreenUser.CancelOrderConfirmation) {
                    popUpTo(AppScreenUser.Orders.toString()) { inclusive = false }
                }
            }
            is UiState.Error -> {
                val raw = (cancelOrderState as UiState.Error).message
                logger.e { "Cancel order error: $raw" }
                showToast(ErrorUtils.sanitizeError(raw))
            }
            else -> {}
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Cancel Order?") },
            text = { Text("Are you sure you want to cancel this order? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        orderViewModel.cancelOrder(orderId)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Go Back")
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Please select a reason for cancellation:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(bottom = 16.dp,top=16.dp)
                    .fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    reasons.forEach { reason ->
                        ReasonRow(
                            text = reason,
                            isSelected = selectedReason == reason,
                            onSelected = { selectedReason = reason }
                        )
                    }
                    ReasonRow(
                        text = "Others",
                        isSelected = isOtherSelected,
                        onSelected = { selectedReason = "Others" }
                    )
                }
            }

            if (isOtherSelected) {
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = otherReasonText,
                    onValueChange = { otherReasonText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    label = { Text("Please specify your reason") },
                    placeholder = { Text("Write here...") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        // Use surface, not surfaceVariant, for a cleaner look
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    showConfirmDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = selectedReason != null &&
                        (!isOtherSelected || otherReasonText.isNotBlank()) &&
                        (cancelOrderState !is UiState.Loading),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                if (cancelOrderState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text(
                        "Cancel Order",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go Back", color = MaterialTheme.colorScheme.primary)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            snackbar = { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        )
    }
}