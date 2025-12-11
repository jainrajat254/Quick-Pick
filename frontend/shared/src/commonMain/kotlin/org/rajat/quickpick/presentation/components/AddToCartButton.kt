package org.rajat.quickpick.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.viewmodel.CartViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun AddToCartButton(
    menuItemId: String,
    menuItemName: String,
    currentQuantity: Int = 0,
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = koinInject()
) {
    var quantity by remember { mutableStateOf(currentQuantity) }
    val addToCartState by cartViewModel.addToCartState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Monitor add to cart state
    LaunchedEffect(addToCartState) {
        when (val state = addToCartState) {
            is UiState.Success -> {
                showToast("Added to cart")
            }
            is UiState.Error -> {
                showToast(state.message)
            }
            else -> {}
        }
    }

    if (quantity == 0) {
        // Show "Add to Cart" button
        Button(
            onClick = {
                quantity = 1
                coroutineScope.launch {
                    cartViewModel.addToCart(menuItemId, 1)
                }
            },
            modifier = modifier.height(40.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Add to Cart",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add", fontWeight = FontWeight.Medium)
        }
    } else {
        // Show quantity selector
        Card(
            modifier = modifier.height(40.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        if (quantity > 1) {
                            quantity--
                            coroutineScope.launch {
                                cartViewModel.updateCartItem(menuItemId, quantity)
                            }
                        } else {
                            quantity = 0
                            coroutineScope.launch {
                                cartViewModel.removeFromCart(menuItemId)
                            }
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Decrease quantity",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = quantity.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                IconButton(
                    onClick = {
                        quantity++
                        coroutineScope.launch {
                            cartViewModel.updateCartItem(menuItemId, quantity)
                        }
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Increase quantity",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

