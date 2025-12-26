package org.rajat.quickpick.presentation.feature.vendor


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemResponse
import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse
import org.rajat.quickpick.presentation.components.ErrorState
import org.rajat.quickpick.presentation.feature.vendor.components.CategoryCard
import org.rajat.quickpick.presentation.feature.vendor.components.OffersSection
import org.rajat.quickpick.presentation.feature.vendor.components.VendorHeaderSection
import org.rajat.quickpick.presentation.feature.vendor.components.VendorNotFound
import org.rajat.quickpick.presentation.feature.menuitem.components.MenuItemCard
import org.rajat.quickpick.presentation.feature.vendor.reviews.ReviewItemCard
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast
import kotlin.math.roundToInt
import org.rajat.quickpick.utils.ErrorUtils

private val vendorScreenLogger = Logger.withTag("VendorScreen")

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
    val reviewsState by reviewViewModel.vendorReviewsState.collectAsState()

    LaunchedEffect(vendorId) {
        vendorScreenLogger.d { "VendorScreen Launched for vendorId=$vendorId" }
        vendorViewModel.getVendorsDetails(vendorId)
        reviewViewModel.getVendorRating(vendorId)
        reviewViewModel.getVendorReviewsPaginated(vendorId, page = 0, size = 50)
        menuItemViewModel.getVendorMenu(vendorId)
    }

    LaunchedEffect(vendorDetailsState) {
        when (vendorDetailsState) {
            is UiState.Error -> {
                val raw = (vendorDetailsState as UiState.Error).message
                vendorScreenLogger.e { "Vendor details load error: $raw" }
                showToast(ErrorUtils.sanitizeError(raw))
            }
            else -> Unit
        }
    }

    LaunchedEffect(ratingState) {
        vendorScreenLogger.d { "VendorScreen ratingState changed: $ratingState" }
    }

    LaunchedEffect(reviewsState) {
        vendorScreenLogger.d { "VendorScreen reviewsState changed: ${reviewsState::class.simpleName}" }
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
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        when (vendorDetailsState) {
            is UiState.Loading -> {
                // Show scrollable loading placeholders
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Header placeholder
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(16.dp),
                            tonalElevation = 0.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {}
                    }

                    // Offers placeholder
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .padding(horizontal = 16.dp),
                            tonalElevation = 0.dp,
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {}
                    }

                    // Tab placeholder
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            tonalElevation = 0.dp,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {}
                    }

                    // Menu items placeholders
                    repeat(8) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .padding(horizontal = 16.dp),
                                tonalElevation = 0.dp,
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {}
                        }
                    }
                }
            }

            is UiState.Error -> {
                val raw = (vendorDetailsState as UiState.Error).message
                vendorScreenLogger.e { "Vendor details load error (UI state): $raw" }
                ErrorState(
                    message = ErrorUtils.sanitizeError(raw),
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
                        .padding(paddingValues)
                        .navigationBarsPadding(),
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
                            is UiState.Loading, UiState.Empty -> {
                                item {
                                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                repeat(8) {
                                    item {
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(100.dp)
                                                .padding(horizontal = 16.dp),
                                            tonalElevation = 0.dp,
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        ) {}
                                    }
                                }
                            }
                            is UiState.Success -> {
                                val itemsList = (vendorMenuState as UiState.Success<List<CreateMenuItemResponse>>).data

                                if (itemsList.isEmpty()) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    "No items available",
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    item {
                                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                            Text(
                                                text = "All items: ${itemsList.size}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
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
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                            }
                            is UiState.Error -> item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(ErrorUtils.sanitizeError((vendorMenuState as UiState.Error).message), color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    } else {
                        // Categories tab
                        if (categories.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No categories available", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        } else {
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
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    navController: NavController,
    reviewViewModel: ReviewViewModel,
    vendorId: String,
    onBackClick: () -> Unit = { navController.navigateUp() }
) {
    val ratingState by reviewViewModel.vendorRatingState.collectAsState()
    val reviewsState by reviewViewModel.vendorReviewsState.collectAsState()

    LaunchedEffect(vendorId) {
        vendorScreenLogger.d { "ReviewsScreen Launched for vendorId=$vendorId" }
        reviewViewModel.getVendorRating(vendorId)
        reviewViewModel.getVendorReviewsPaginated(vendorId, page = 0, size = 50)
    }

    LaunchedEffect(ratingState) {
        vendorScreenLogger.d { "ReviewsScreen ratingState changed: $ratingState" }
    }

    LaunchedEffect(reviewsState) {
        vendorScreenLogger.d { "ReviewsScreen reviewsState changed: ${reviewsState::class.simpleName}" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reviews", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }

            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            when (ratingState) {
                is UiState.Success -> {
                    val rating = (ratingState as UiState.Success).data
                    if (rating.averageRating != null && rating.totalReviews != null) {
                        Text(
                            text = "${(rating.averageRating * 10).roundToInt() / 10.0} average from ${rating.totalReviews} reviews",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                is UiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                is UiState.Error -> Text("Failed to load rating", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
                UiState.Empty -> {}
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (reviewsState) {
                is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(ErrorUtils.sanitizeError((reviewsState as UiState.Error).message), color = MaterialTheme.colorScheme.error)
                }
                is UiState.Success -> {
                    val reviews = (reviewsState as UiState.Success).data.content?.filterNotNull() ?: emptyList()
                    if (reviews.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No reviews yet") }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(reviews, key = { it.id ?: it.userId ?: it.vendorId ?: it.hashCode().toString() }) { review ->
                                ReviewItemCard(review)
                            }
                        }
                    }
                }
                UiState.Empty -> {}
            }
        }
    }
}
