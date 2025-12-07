package org.rajat.quickpick.presentation.feature.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.feature.cart.components.CartItemRow
import org.rajat.quickpick.presentation.feature.cart.components.CartSummary
import org.rajat.quickpick.presentation.feature.cart.components.EmptyCartView
import org.rajat.quickpick.presentation.viewmodel.CartViewModel
import org.rajat.quickpick.utils.BackHandler
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.exitApp
import org.rajat.quickpick.utils.toast.showToast

@OptIn(ExperimentalTime::class)
@Composable
fun CartScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    cartViewModel: CartViewModel = koinInject()
) {
    val cartState by cartViewModel.cartState.collectAsState()
    val updateCartState by cartViewModel.updateCartState.collectAsState()
    val removeFromCartState by cartViewModel.removeFromCartState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var backPressedTime by remember { mutableStateOf(0L) }

    // Double back press to exit
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
        cartViewModel.getCart()
    }

    LaunchedEffect(updateCartState) {
        when (updateCartState) {
            is UiState.Success -> {
                // Cart automatically updated via main cart state
            }
            is UiState.Error -> {
                showToast((updateCartState as UiState.Error).message)
            }
            else -> {}
        }
    }

    LaunchedEffect(removeFromCartState) {
        when (removeFromCartState) {
            is UiState.Success -> {
                showToast("Item removed from cart")
            }
            is UiState.Error -> {
                showToast((removeFromCartState as UiState.Error).message)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
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
                val cartItems = cart.items

                if (cartItems.isEmpty()) {
                    EmptyCartView()
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(cartItems, key = { it.menuItemId ?: "" }) { item ->
                            CartItemRow(
                                item = CartItem(
                                    id = item.menuItemId ?: "",
                                    name = item.menuItemName ?: "",
                                    price = item.unitPrice,
                                    quantity = item.quantity,
                                    imageUrl = item.menuItemImage
                                ),
                                onQuantityChange = { itemId, newQuantity ->
                                    coroutineScope.launch {
                                        cartViewModel.updateCartItem(itemId, newQuantity)
                                    }
                                },
                                onRemoveItem = { itemId ->
                                    coroutineScope.launch {
                                        cartViewModel.removeFromCart(itemId)
                                    }
                                },
                                navController = navController
                            )
                        }
                    }

                    CartSummary(
                        totalAmount = cart.totalAmount,
                        navController = navController,
                        vendorId = cart.vendorId,
                        vendorName = cart.vendorName
                    )
                }
            }
            is UiState.Error -> {
                EmptyCartView()
            }
            is UiState.Empty -> {
                EmptyCartView()
            }
        }
    }
}
