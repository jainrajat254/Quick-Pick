package org.rajat.quickpick.presentation.feature.menuitem.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.menuitems.MenuItem
import org.rajat.quickpick.presentation.viewmodel.CartViewModel
import org.rajat.quickpick.utils.ErrorUtils
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

private val logger = Logger.withTag("MenuItemCard")

@Composable
fun MenuItemCard(
    menuItem: MenuItem,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = koinInject()
) {
    val cartState by cartViewModel.cartState.collectAsState()
    val addToCartState by cartViewModel.addToCartState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val quantity = remember(cartState, menuItem.id) {
        if (cartState is UiState.Success) {
            val cart = (cartState as UiState.Success).data
            cart.items.find { it.menuItemId == menuItem.id }?.quantity ?: 0
        } else {
            0
        }
    }

    LaunchedEffect(addToCartState) {
        when (val state = addToCartState) {
            is UiState.Success -> {
                showToast("Added to cart")
            }
            is UiState.Error -> {
                val raw = state.message
                logger.e { "Add to cart error: $raw" }
                showToast(ErrorUtils.sanitizeError(raw))
            }
            else -> {}
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            MenuItemImage(
                imageUrl = menuItem.imageUrl,
                itemName = menuItem.name ?: "Item",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(100.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        VegNonVegBadge(isVeg = menuItem.isVeg ?: false)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = menuItem.name ?: "Item",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (!menuItem.description.isNullOrBlank()) {
                        Text(
                            text = menuItem.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${menuItem.price}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (menuItem.available == true) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ){
                            FilledTonalIconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        if (quantity == 1) {
                                            // Remove from cart if quantity is 1
                                            cartViewModel.removeFromCart(menuItem.id ?: "")
                                        } else if (quantity > 1) {
                                            // Decrease quantity
                                            cartViewModel.updateCartItem(menuItem.id ?: "", quantity - 1)
                                        }
                                    }
                                },
                                modifier = Modifier.size(36.dp),
                                enabled = quantity > 0,
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Remove,
                                    contentDescription = "Remove from cart",
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Show quantity if > 0
                            if (quantity > 0) {
                                Text(
                                    text = quantity.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            FilledTonalIconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        if (quantity == 0) {
                                            // Add to cart for the first time
                                            cartViewModel.addToCart(menuItem.id ?: "", 1)
                                        } else {
                                            // Increase quantity
                                            cartViewModel.updateCartItem(menuItem.id ?: "", quantity + 1)
                                        }
                                    }
                                },
                                modifier = Modifier.size(36.dp),
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = "Add to cart",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = "Out of Stock",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
