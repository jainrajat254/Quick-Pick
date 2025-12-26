package org.rajat.quickpick.presentation.feature.myorders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.feature.myorders.components.OrderList
import org.rajat.quickpick.presentation.feature.myorders.components.OrderTab
import org.rajat.quickpick.presentation.feature.myorders.components.StyledTabRow
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.CartViewModel
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.BackHandler
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.exitApp
import org.rajat.quickpick.utils.toast.showToast
import co.touchlab.kermit.Logger
import org.rajat.quickpick.presentation.feature.payment.openRazorpayCheckout
import org.rajat.quickpick.presentation.feature.payment.getPlatformActivityForPayment
import org.rajat.quickpick.utils.ErrorUtils

private val razorLogger = Logger.withTag("RAZORPAYDEBUG")
private val logger = Logger.withTag("MyOrdersScreen")

@OptIn(ExperimentalTime::class)
@Composable
fun MyOrderScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    orderViewModel: OrderViewModel = koinInject(),
    cartViewModel: CartViewModel = koinInject()
) {
    val platformActivity = getPlatformActivityForPayment()

    val tabs = listOf(OrderTab.Active, OrderTab.Completed, OrderTab.Cancelled)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var backPressedTime by remember { mutableStateOf(0L) }

    val myOrdersState by orderViewModel.myOrdersState.collectAsState()

    BackHandler(enabled = true) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - backPressedTime < 2000) {
            exitApp()
        } else {
            backPressedTime = currentTime
            showToast("Press back again to exit")
        }
    }

    LaunchedEffect(Unit) {
        orderViewModel.getMyOrders()
    }

    LaunchedEffect(myOrdersState) {
        if (myOrdersState is UiState.Error) {
            val raw = (myOrdersState as UiState.Error).message
            logger.e { "MyOrders load error: $raw" }
            showToast(ErrorUtils.sanitizeError(raw))
        }
    }

    val paymentUiState = orderViewModel.paymentUiState.collectAsState()
    val paymentSuccessEvent by orderViewModel.paymentSuccessEvent.collectAsState()
    LaunchedEffect(paymentUiState.value) {
        razorLogger.d { "paymentUiState changed: ${paymentUiState.value}" }
        val resp = paymentUiState.value
        if (resp != null) {
            if (!resp.transactionId.isNullOrBlank()) {
                razorLogger.d { "MyOrdersScreen: payment initiated with transactionId=${resp.transactionId}, key=${resp.razorpayKeyId}" }
                if (platformActivity != null && !resp.razorpayKeyId.isNullOrBlank()) {
                    razorLogger.d { "MyOrdersScreen: calling openRazorpayCheckout with activity and order=${resp.transactionId}" }
                    try {
                        val amountInPaise = resp.amount?.times(100)?.toLong()
                        openRazorpayCheckout(platformActivity, resp.razorpayKeyId, resp.transactionId, amountInPaise)
                        razorLogger.d { "MyOrdersScreen: openRazorpayCheckout invoked" }
                    } catch (e: Exception) {
                        razorLogger.d { "MyOrdersScreen: openRazorpayCheckout exception: ${e.message}" }
                    }
                } else {
                    razorLogger.d { "MyOrdersScreen: razorpayKeyId not provided, cannot open checkout" }
                }
            } else {
                razorLogger.d { "MyOrdersScreen: paymentUiState present but transactionId is null for order=${resp.orderId}" }
            }
        }
    }

    val allOrders = when (myOrdersState) {
        is UiState.Success -> (myOrdersState as UiState.Success).data.orders?.filterNotNull() ?: emptyList()
        else -> emptyList()
    }

    val activeOrders = allOrders.filter {
        it.orderStatus in listOf("PENDING", "ACCEPTED", "PREPARING", "READY")
    }

    val completedOrders = allOrders.filter {
        it.orderStatus == "COMPLETED"
    }

    val cancelledOrders = allOrders.filter {
        it.orderStatus == "CANCELLED"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StyledTabRow(
            tabs = tabs.map { it.title },
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            when (val state = myOrdersState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                is UiState.Success -> {
                    val currentOrders = when (tabs[selectedTabIndex]) {
                        is OrderTab.Active -> activeOrders
                        is OrderTab.Completed -> completedOrders
                        is OrderTab.Cancelled -> cancelledOrders
                    }

                    OrderList(
                         orders = currentOrders,
                         tabName = tabs[selectedTabIndex].title,
                        onOrderCancel = {
                            razorLogger.d { "MyOrdersScreen: onOrderCancel clicked for order=$it" }
                            navController.navigate(
                                AppScreenUser.CancelOrder(orderId = it)
                            )
                        },
                        onOrderRate = {order->
                            razorLogger.d { "MyOrdersScreen: onOrderRate clicked for order=${order.id}" }
                            val orderId = order.id
                            val firstItem = order.orderItems?.firstOrNull()
                            val itemName = firstItem?.menuItemName ?: "Item"
                            val itemImageUrl =  ""

                            if (orderId != null) {
                                navController.navigate(
                                    AppScreenUser.ReviewOrder(
                                        orderId = orderId,
                                        itemName = itemName,
                                        itemImageUrl = itemImageUrl
                                    )
                                )
                            } else {
                                showToast("Error: Cannot review this order.")
                            }
                        },
                        onOrderAgain = { order ->
                            razorLogger.d { "MyOrdersScreen: onOrderAgain clicked for order=${order.id}" }
                            cartViewModel.clearCart()
                            order.orderItems
                                ?.filterNotNull()
                                ?.forEach { item ->

                                    val id = item.menuItemId ?: return@forEach
                                    val qty = item.quantity ?: return@forEach

                                    cartViewModel.addToCart(
                                        menuItemId = id,
                                        quantity = qty
                                    )
                                }

                            navController.navigate(AppScreenUser.Cart)
                        },
                        onOrderViewDetails = {
                            razorLogger.d { "MyOrdersScreen: onOrderViewDetails clicked for order=$it" }
                            navController.navigate(AppScreenUser.OrderDetail(
                                orderId = it
                            ))
                        },
                        onclick = {
                            razorLogger.d { "MyOrdersScreen: onclick order card for order=$it" }
                            navController.navigate(AppScreenUser.OrderDetail(
                                orderId = it
                            )
                            )
                        },
                        onPayNow = { orderId ->
                            razorLogger.d { "MyOrdersScreen: Pay Now clicked for orderId=$orderId" }
                            orderViewModel.initiatePayment(orderId, paymentMethod = "PAY_NOW")
                        },
                        paymentUiState = paymentUiState.value
                    )


                    if (paymentSuccessEvent != null) {
                        val paidOrder = allOrders.firstOrNull { it.id == paymentSuccessEvent }
                        val otpText = paidOrder?.otp ?: ""
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { orderViewModel.resetPaymentSuccessEvent() },
                            title = { Text("Payment Successful") },
                            text = { Text("Your payment was successful. Pick up your order. OTP: $otpText") },
                            confirmButton = {
                                Button(onClick = { orderViewModel.resetPaymentSuccessEvent() }) {
                                    Text("OK")
                                }
                            }
                        )
                    }

                }
                is UiState.Error -> {
                    val raw = state.message
                    logger.e { "MyOrders UI error display: $raw" }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(top = 32.dp)
                    ) {
                        Text(
                            text = "Failed to load orders",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = ErrorUtils.sanitizeError(raw),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                is UiState.Empty -> {
                    Text(
                        text = "No orders yet",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }
        }
    }
}