package org.rajat.quickpick.presentation.feature.menuitem

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.domain.modal.search.SearchMenuItemsResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.ErrorState
import org.rajat.quickpick.presentation.feature.menuitem.components.FilterSortBottomSheet
import org.rajat.quickpick.presentation.feature.menuitem.components.MenuItemCard
import org.rajat.quickpick.presentation.feature.menuitem.components.MenuItemEmptyState
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemScreen(
    navController: NavController? = null,
    menuItemViewModel: MenuItemViewModel,
    vendorId: String,
    category: String,
    onBackClick: () -> Unit = { navController?.navigateUp() }
) {
    val menuItemsState by menuItemViewModel.menuItemsState.collectAsState()
    val minPrice by menuItemViewModel.minPrice.collectAsState()
    val maxPrice by menuItemViewModel.maxPrice.collectAsState()
    val availableOnly by menuItemViewModel.availableOnly.collectAsState()
    val sortBy by menuItemViewModel.sortBy.collectAsState()
    val sortDirection by menuItemViewModel.sortDirection.collectAsState()

    var showFilterSheet by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }
    var localSearchQuery by remember { mutableStateOf("") }

    LaunchedEffect(vendorId, category) {
        menuItemViewModel.setVendorAndCategory(vendorId, category)
        menuItemViewModel.getMenuItemsByCategory(vendorId, category)
    }

    LaunchedEffect(menuItemsState) {
        when (menuItemsState) {
            is UiState.Error -> {
                val message = (menuItemsState as UiState.Error).message ?: "Failed to load menu items"
                showToast(message)
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (showSearchBar) {
                        TextField(
                            value = localSearchQuery,
                            onValueChange = {
                                localSearchQuery = it
                                menuItemViewModel.updateSearchQuery(it)
                            },
                            placeholder = { Text("Search items...") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Column {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showSearchBar) {
                            showSearchBar = false
                            localSearchQuery = ""
                            menuItemViewModel.updateSearchQuery("")
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (!showSearchBar) {
                        IconButton(onClick = { showSearchBar = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                    IconButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter & Sort"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        when (menuItemsState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CustomLoader()
                }
            }

            is UiState.Error -> {
                ErrorState(
                    message = (menuItemsState as UiState.Error).message
                        ?: "Failed to load menu items",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            UiState.Empty -> {
                MenuItemEmptyState(
                    category = category,
                    onBackClick = onBackClick,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            is UiState.Success -> {
                val response = (menuItemsState as UiState.Success<org.rajat.quickpick.domain.modal.menuitems.GetVendorMenuByCategoryResponse>).data
                val menuItems = response.menuItems?.filterNotNull() ?: emptyList()

                if (menuItems.isEmpty()) {
                    MenuItemEmptyState(
                        category = category,
                        onBackClick = onBackClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "${menuItems.size} items available",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(
                            items = menuItems,
                            key = { it.id ?: "unknown_${it.hashCode()}" }
                        ) { menuItem ->
                            MenuItemCard(
                                menuItem = org.rajat.quickpick.domain.modal.menuitems.MenuItem(
                                    available = menuItem.available,
                                    category = menuItem.category,
                                    createdAt = menuItem.createdAt,
                                    description = menuItem.description,
                                    id = menuItem.id,
                                    imageUrl = menuItem.imageUrl,
                                    name = menuItem.name,
                                    price = menuItem.price ?: 0.0,
                                    quantity = menuItem.quantity,
                                    updatedAt = menuItem.updatedAt,
                                    veg = menuItem.veg,
                                    vendorId = menuItem.vendorId
                                ),
                                onItemClick = {

                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        if (showFilterSheet) {
            FilterSortBottomSheet(
                minPrice = minPrice,
                maxPrice = maxPrice,
                availableOnly = availableOnly,
                sortBy = sortBy,
                sortDirection = sortDirection,
                onMinPriceChange = { menuItemViewModel.updatePriceRange(it, maxPrice) },
                onMaxPriceChange = { menuItemViewModel.updatePriceRange(minPrice, it) },
                onAvailableOnlyChange = { menuItemViewModel.updateAvailableOnly(it) },
                onSortChange = { sortByValue, sortDirectionValue ->
                    menuItemViewModel.updateSort(sortByValue, sortDirectionValue)
                },
                onApply = {
                    menuItemViewModel.searchMenuItems()
                },
                onReset = {
                    menuItemViewModel.resetFilters()
                    menuItemViewModel.searchMenuItems()
                },
                onDismiss = { showFilterSheet = false }
            )
        }
    }
}