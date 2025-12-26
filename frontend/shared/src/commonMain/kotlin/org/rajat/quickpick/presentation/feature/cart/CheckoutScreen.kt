package org.rajat.quickpick.presentation.feature.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.ordermanagement.getOrderById.GetOrderByIdResponse
import org.rajat.quickpick.presentation.feature.cart.components.CreateOrderBody
import org.rajat.quickpick.presentation.feature.cart.components.OrderInfoHeader
import org.rajat.quickpick.presentation.feature.cart.components.PaymentMethod
import org.rajat.quickpick.presentation.feature.cart.components.SpecialInstructions
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.CartViewModel
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.ErrorUtils
import co.touchlab.kermit.Logger

private val logger = Logger.withTag("CheckoutScreen")

@Composable
fun CheckoutScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    cartViewModel: CartViewModel = koinInject(),
    orderViewModel: OrderViewModel = koinInject()
) {
    var specialInstructions by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("PAY_ON_DELIVERY") }
    val cartState by cartViewModel.cartState.collectAsState()
    val createOrderState by orderViewModel.createOrderState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        cartViewModel.getCart()
    }

    LaunchedEffect(createOrderState) {
        when (createOrderState) {
            is UiState.Success -> {
                showToast("Order placed successfully!")
                val successfulOrderId =
                    (createOrderState as UiState.Success<GetOrderByIdResponse>).data.id!!

                cartViewModel.clearCart()
                orderViewModel.resetCreateOrderState()

                navController.navigate(
                    AppScreenUser.ConfirmOrder(orderId = successfulOrderId)
                ) {
                    popUpTo(AppScreenUser.Cart) { inclusive = true }
                }
            }
            is UiState.Error -> {
                val raw = (createOrderState as UiState.Error).message
                logger.e { "Create order error: $raw" }
                showToast(ErrorUtils.sanitizeError(raw))
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        when (val state = cartState) {
            is UiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            is UiState.Success -> {
                val cart = state.data
                val cartItems = cart.items.map {
                    CartItem(
                        id = it.menuItemId ?: "",
                        name = it.menuItemName ?: "",
                        price = it.unitPrice,
                        quantity = it.quantity,
                        imageUrl = it.menuItemImage
                    )
                }

                OrderInfoHeader(
                    cartItems = cartItems,
                    totalAmount = cart.totalAmount,
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Pass selected payment method state down to composable
                PaymentMethod(selectedMethod = selectedPaymentMethod, onMethodSelected = { selectedPaymentMethod = it })

                SpecialInstructions(
                    specialInstructions = specialInstructions,
                    onSpecialInstructionsChange = { specialInstructions = it }
                )

                Spacer(modifier = Modifier.weight(1f))

                CreateOrderBody(
                    totalAmount = cart.totalAmount,
                    navController = navController,
                    specialInstructions = specialInstructions,
                    isLoading = createOrderState is UiState.Loading,
                    cartItems = cartItems,
                    onPlaceOrder = {
                        coroutineScope.launch {
                            orderViewModel.createOrderFromCart()
                        }
                    }
                )
            }
            is UiState.Error, is UiState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Cart is empty",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
