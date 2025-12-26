package org.rajat.quickpick.presentation.feature.vendor.orders

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.navigation.NavController
import org.rajat.quickpick.domain.modal.ordermanagement.UpdateOrderStateRequest
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.feature.vendor.orders.components.VendorOrderCard
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.BackHandler
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.exitApp
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.ErrorUtils

@OptIn(ExperimentalTime::class)
@Composable
fun VendorOrdersScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    orderViewModel: OrderViewModel
) {
    var selectedTab by remember { mutableStateOf(0) }
    var backPressedTime by remember { mutableStateOf(0L) }
    val tabs = listOf("Pending", "Accepted", "Completed")

    val initialRequestedTab by orderViewModel.initialVendorOrdersTab.collectAsState()
    var hasAppliedInitialTab by remember { mutableStateOf(false) }

    val pendingOrdersState by orderViewModel.pendingOrdersState.collectAsState()
    val updateOrderStatusState by orderViewModel.updateOrderStatusState.collectAsState()
    val vendorOrdersByStatusState by orderViewModel.vendorOrdersByStatusState.collectAsState()
    val vendorAcceptedCombinedState by orderViewModel.vendorOrdersAcceptedCombinedState.collectAsState()

    var currentOtpDialogOrderId by remember { mutableStateOf<String?>(null) }
    var currentDialogOtpInput by remember { mutableStateOf("") }

    LaunchedEffect(initialRequestedTab) {
        if (!hasAppliedInitialTab && initialRequestedTab != null) {
            val idx = initialRequestedTab!!.coerceIn(0, tabs.size - 1)
            selectedTab = idx
            hasAppliedInitialTab = true
            orderViewModel.resetInitialVendorOrdersTab()
        }
    }

    BackHandler(enabled = true) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - backPressedTime < 2000) {
            exitApp()
        } else {
            backPressedTime = currentTime
            showToast("Press back again to exit")
        }
    }

    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            0 -> orderViewModel.getVendorOrdersByStatus("PENDING")
            1 -> {
                orderViewModel.getCombinedAcceptedOrders()
                orderViewModel.getVendorOrdersByStatus("PREPARING")
            }
            2 -> orderViewModel.getVendorOrdersByStatus("COMPLETED")
            else -> orderViewModel.getVendorOrdersByStatus("PENDING")
        }
    }


    LaunchedEffect(updateOrderStatusState) {
        when (updateOrderStatusState) {
            is UiState.Success -> {
                showToast("Order marked as completed")
                orderViewModel.getVendorOrdersByStatus("PENDING")
                orderViewModel.getPendingOrdersForVendor()
                currentOtpDialogOrderId = null
                currentDialogOtpInput = ""
                orderViewModel.resetUpdateOrderStatusState()
            }
            is UiState.Error -> {
                val raw = (updateOrderStatusState as UiState.Error).message
                val message = ErrorUtils.sanitizeError(raw)
                showToast(message)
                orderViewModel.resetUpdateOrderStatusState()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                    },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1
                        )
                    }
                )
            }
        }

        if (selectedTab == 0 || selectedTab == 1 || selectedTab == 2) {
            var otpInput by remember { mutableStateOf("") }
            var searchInProgress by remember { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = otpInput,
                    onValueChange = { input ->
                        // accept only digits and limit to 4 chars
                        val filtered = input.filter { it.isDigit() }.take(4)
                        otpInput = filtered
                    },
                    modifier = Modifier.weight(1f),
                    label = { Text("Search by OTP") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Button(
                    onClick = {
                        if (otpInput.length == 4) {
                            searchInProgress = true
                            // Reuse pending search endpoint which locates order by OTP and navigates to it
                            orderViewModel.getPendingOrdersForVendor(otpInput)
                        } else {
                            showToast("Enter a 4-digit OTP to search")
                        }
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Search")
                }
            }

            // React to pendingOrdersState result only when searching
            LaunchedEffect(pendingOrdersState, searchInProgress) {
                if (!searchInProgress) return@LaunchedEffect

                when (pendingOrdersState) {
                    is UiState.Loading -> Unit
                    is UiState.Success -> {
                        val orders = (pendingOrdersState as UiState.Success).data.orders?.filterNotNull() ?: emptyList()
                        if (orders.isNotEmpty()) {
                            val first = orders.first()
                            first.id?.let { orderId ->
                                // Navigate to the order detail screen for the found order
                                navController.navigate(AppScreenVendor.VendorOrderDetail(orderId = orderId))
                            }
                        } else {
                            showToast("No order found for this OTP")
                        }
                        searchInProgress = false
                    }
                    is UiState.Error -> {
                        val raw = (pendingOrdersState as UiState.Error).message
                        val message = ErrorUtils.sanitizeError(raw)
                        showToast(message)
                        searchInProgress = false
                    }
                    else -> Unit
                }
            }
        }

        // Determine loading & error states. For Accepted tab, prefer vendorAcceptedCombinedState
        val isLoading = if (selectedTab == 1) vendorAcceptedCombinedState is UiState.Loading else vendorOrdersByStatusState is UiState.Loading
        val isErrorState = if (selectedTab == 1) vendorAcceptedCombinedState is UiState.Error else vendorOrdersByStatusState is UiState.Error

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CustomLoader() }
        } else if (isErrorState) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Error loading orders", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                    Button(onClick = {
                        when (selectedTab) {
                            0 -> orderViewModel.getVendorOrdersByStatus("PENDING")
                            1 -> { orderViewModel.getVendorOrdersByStatus("ACCEPTED") }
                            2 -> orderViewModel.getVendorOrdersByStatus("COMPLETED")
                            else -> orderViewModel.getVendorOrdersByStatus("PENDING")
                        }
                    }) { Text("Retry") }
                }
            }
        } else {
            // Build final orders list; Accepted tab uses vendorAcceptedCombinedState primarily
            val statusOrders = (vendorOrdersByStatusState as? UiState.Success)?.data?.orders?.filterNotNull() ?: emptyList()
            val combined = when (vendorAcceptedCombinedState) {
                is UiState.Success -> (vendorAcceptedCombinedState as UiState.Success).data
                else -> emptyList()
            }

            val orders = when (selectedTab) {
                0 -> statusOrders
                1 -> {
                    if (vendorAcceptedCombinedState is UiState.Success) {
                        (combined + statusOrders).distinctBy { it.id }
                    } else {
                        statusOrders
                    }
                }
                2 -> statusOrders
                else -> emptyList()
            }

            if (orders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val emptyMessage = when (selectedTab) {
                        0 -> "No pending orders"
                        1 -> "No accepted/preparing/ready orders"
                        2 -> "No completed orders"
                        else -> "No orders"
                    }
                    Text(text = emptyMessage, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .navigationBarsPadding(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(orders.size) { index ->
                        val order = orders[index]

                        val density = LocalDensity.current
                        val thresholdPx = with(density) { 120.dp.toPx() }
                        val offsetX = remember { Animatable(0f) }
                        val coroutineScope = rememberCoroutineScope()
                        var shouldShowDialog by remember { mutableStateOf(false) }

                        LaunchedEffect(shouldShowDialog) {
                            if (shouldShowDialog) {
                                currentOtpDialogOrderId = order.id
                                currentDialogOtpInput = ""
                                shouldShowDialog = false
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                                .pointerInput(order.id) {
                                    detectHorizontalDragGestures(
                                        onHorizontalDrag = { change, dragAmount ->
                                            change.consume()
                                            coroutineScope.launch {
                                                val newOffset = (offsetX.value + dragAmount).coerceIn(0f, thresholdPx * 1.5f)
                                                offsetX.snapTo(newOffset)
                                            }
                                        },
                                        onDragEnd = {
                                            coroutineScope.launch {
                                                val currentValue = offsetX.value
                                                if (currentValue > thresholdPx) {
                                                    // Snap back with animation
                                                    offsetX.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = tween(durationMillis = 300)
                                                    )
                                                    // Show dialog after animation
                                                    shouldShowDialog = true
                                                } else {
                                                    // Just snap back
                                                    offsetX.animateTo(
                                                        targetValue = 0f,
                                                        animationSpec = tween(durationMillis = 300)
                                                    )
                                                }
                                            }
                                        },
                                        onDragCancel = {
                                            coroutineScope.launch {
                                                offsetX.animateTo(
                                                    targetValue = 0f,
                                                    animationSpec = tween(durationMillis = 300)
                                                )
                                            }
                                        }
                                    )
                                }
                        ) {
                            VendorOrderCard(
                                order = order,
                                onClick = {
                                    order.id?.let { orderId ->
                                        navController.navigate(AppScreenVendor.VendorOrderDetail(orderId = orderId))
                                    }
                                }
                            )

                            if (currentOtpDialogOrderId == order.id) {
                                AlertDialog(
                                    onDismissRequest = { currentOtpDialogOrderId = null },
                                    title = { Text("Enter OTP to complete") },
                                    text = {
                                        Column {
                                            OutlinedTextField(
                                                value = currentDialogOtpInput,
                                                onValueChange = { input -> currentDialogOtpInput = input.filter { it.isDigit() }.take(4) },
                                                label = { Text("4-digit OTP") },
                                                singleLine = true,
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Ask the customer for the OTP and enter it here. The vendor will not be shown the OTP beforehand.",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                if (currentDialogOtpInput.length == 4) {
                                                    order.id?.let { id ->
                                                        orderViewModel.updateOrderStatus(
                                                            id,
                                                            UpdateOrderStateRequest(
                                                                orderStatus = "COMPLETED",
                                                                otp = currentDialogOtpInput
                                                            )
                                                        )
                                                    }
                                                } else {
                                                    showToast("Please enter a 4-digit OTP")
                                                }
                                            }
                                        ) {
                                            if (updateOrderStatusState is UiState.Loading) {
                                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = MaterialTheme.colorScheme.onPrimary)
                                            } else {
                                                Text("Mark as Complete")
                                            }
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { currentOtpDialogOrderId = null }) {
                                            Text("Cancel")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
