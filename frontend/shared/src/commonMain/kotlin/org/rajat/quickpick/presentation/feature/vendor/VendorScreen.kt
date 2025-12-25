package org.rajat.quickpick.presentation.feature.vendor


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemResponse
import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.ErrorState
import org.rajat.quickpick.presentation.feature.vendor.components.CategoryCard
import org.rajat.quickpick.presentation.feature.vendor.components.OffersSection
import org.rajat.quickpick.presentation.feature.vendor.components.VendorHeaderSection
import org.rajat.quickpick.presentation.feature.vendor.components.VendorNotFound
import org.rajat.quickpick.presentation.feature.menuitem.components.MenuItemCard
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorScreen(
    navController: NavController,
    vendorViewModel: VendorViewModel,
    menuItemViewModel: MenuItemViewModel,
    reviewViewModel: ReviewViewModel,
    vendorId: String,
    onBackClick: () -> Unit = { navController.navigateUp() }
) {
    val vendorDetailsState by vendorViewModel.vendorsDetailState.collectAsState()
    val ratingState by reviewViewModel.vendorRatingState.collectAsState()
    val vendorMenuState by menuItemViewModel.vendorMenuState.collectAsState()

    LaunchedEffect(vendorId) {
        vendorViewModel.getVendorsDetails(vendorId)
        reviewViewModel.getVendorRating(vendorId)
        // Fetch vendor menu (all items) for the "All" tab
        menuItemViewModel.getVendorMenu(vendorId)
    }

    LaunchedEffect(vendorDetailsState) {
        when (vendorDetailsState) {
            is UiState.Error -> {
                val message = (vendorDetailsState as UiState.Error).message ?: "Failed to load vendor details"
                showToast(message)
            }
            else -> Unit
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val titleText = when (vendorDetailsState) {
                        is UiState.Success -> (vendorDetailsState as UiState.Success<GetVendorByIDResponse>).data.storeName ?: "Store Details"
                        else -> "Store Details"
                    }
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        when (vendorDetailsState) {
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
                    message = (vendorDetailsState as UiState.Error).message
                        ?: "Failed to load vendor details",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            UiState.Empty -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    VendorNotFound(
                        onBackClick = onBackClick
                    )
                }
            }

            is UiState.Success -> {
                val vendor = (vendorDetailsState as UiState.Success<GetVendorByIDResponse>).data
                val categories = vendor.foodCategories?.filterNotNull() ?: emptyList()

                LaunchedEffect(vendor.id) {
                    if (categories.isNotEmpty()) {
                        menuItemViewModel.getVendorMenuByCategories(vendorId, categories)
                    }
                }

                var selectedTab by remember { mutableStateOf(0) }
                val tabs = listOf("All", "Categories")

                var vendorMenuFallbackAttempted by remember { mutableStateOf(false) }

                LaunchedEffect(selectedTab) {
                    if (selectedTab == 0) {
                        menuItemViewModel.getVendorMenu(vendorId)
                    }
                }



                LaunchedEffect(vendorMenuState, categories) {
                    when (vendorMenuState) {
                        is UiState.Success -> {
                            val items = (vendorMenuState as UiState.Success<List<CreateMenuItemResponse>>).data
                            if (items.isEmpty() && categories.isNotEmpty() && !vendorMenuFallbackAttempted) {
                                vendorMenuFallbackAttempted = true
                                menuItemViewModel.getVendorMenuByCategories(vendorId, categories)
                            }
                        }
                        is UiState.Error -> {
                            if (categories.isNotEmpty() && !vendorMenuFallbackAttempted) {
                                vendorMenuFallbackAttempted = true
                                menuItemViewModel.getVendorMenuByCategories(vendorId, categories)
                            }
                        }
                        else -> Unit
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        VendorHeaderSection(
                            vendor = vendor,
                            ratingState = ratingState,
                            onRatingClick = { navController.navigate(AppScreenVendor.VendorReviewsScreen(vendor.id ?: "")) }
                        )
                    }

                    item {
                        OffersSection()
                    }

                    item {
                        PrimaryTabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = {
                                        Text(
                                            text = title,
                                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                )
                            }
                        }
                    }

                    if (selectedTab == 0) {
                        when (vendorMenuState) {
                            is UiState.Loading -> item { Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { CustomLoader() } }
                            is UiState.Success -> {
                                val itemsList = (vendorMenuState as UiState.Success<List<CreateMenuItemResponse>>).data
                                item {
                                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                        Text(
                                            text = "All items: ${itemsList.size}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        val statusText = when (vendorMenuState) {
                                            is UiState.Loading -> "Loading"
                                            is UiState.Success -> "Success"
                                            is UiState.Error -> "Error: ${(vendorMenuState as UiState.Error).message}"
                                            else -> "Empty"
                                        }
                                        Text(text = "status: $statusText", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                if (itemsList.isEmpty()) {
                                    item {
                                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                            Text("No items available", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                } else {
                                    item {
                                        val names = itemsList.take(8).mapNotNull { it.name }
                                        Text(text = "items: ${names.joinToString(", ")}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(horizontal = 16.dp))
                                    }
                                     items(itemsList) { menuResp ->
                                         MenuItemCard(
                                            menuItem = org.rajat.quickpick.domain.modal.menuitems.MenuItem(
                                                available = menuResp.available,
                                                category = menuResp.category,
                                                createdAt = menuResp.createdAt,
                                                description = menuResp.description,
                                                id = menuResp.id,
                                                imageUrl = menuResp.imageUrl,
                                                name = menuResp.name,
                                                price = menuResp.price ?: 0.0,
                                                quantity = menuResp.quantity,
                                                updatedAt = menuResp.updatedAt,
                                                isVeg = menuResp.isVeg,
                                                vendorId = menuResp.vendorId
                                            ),
                                            onItemClick = {
                                                navController.navigate(AppScreenUser.MenuItemCategory(vendorId = vendor.id ?: "", category = menuResp.category ?: ""))
                                            }
                                        )
                                    }
                                    item{
                                        Spacer(modifier = Modifier.height(60.dp))
                                    }
                                }
                            }
                            is UiState.Error -> item { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text((vendorMenuState as UiState.Error).message ?: "Error loading items", color = MaterialTheme.colorScheme.error) } }
                            UiState.Empty -> item { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CustomLoader() } }
                        }
                    } else {
                        // selectedTab == 1 -> Categories: show existing categories grid as before
                        val categoriesInRows = categories.chunked(2)
                        items(categoriesInRows) { rowCategories ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (rowCategories.isNotEmpty()) {
                                    CategoryCard(
                                        category = rowCategories[0],
                                        onClick = {
                                            navController.navigate(
                                                AppScreenUser.MenuItemCategory(
                                                    vendorId = vendorId,
                                                    category = rowCategories[0]
                                                )
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                if (rowCategories.size > 1) {
                                    CategoryCard(
                                        category = rowCategories[1],
                                        onClick = {
                                            navController.navigate(
                                                AppScreenUser.MenuItemCategory(
                                                    vendorId = vendorId,
                                                    category = rowCategories[1]
                                                )
                                            )
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(60.dp))
                        }
                    }
                }
            }
        }
    }
}
