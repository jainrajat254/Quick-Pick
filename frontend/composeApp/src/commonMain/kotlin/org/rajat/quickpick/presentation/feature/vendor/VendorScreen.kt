package org.rajat.quickpick.presentation.feature.vendor


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.data.dummy.DummyData
import org.rajat.quickpick.domain.modal.search.GetVendorByIDResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.ErrorState
import org.rajat.quickpick.presentation.feature.menuitem.MenuItemScreen
import org.rajat.quickpick.presentation.feature.vendor.components.CategoryCard
import org.rajat.quickpick.presentation.feature.vendor.components.OffersSection
import org.rajat.quickpick.presentation.feature.vendor.components.VendorHeaderSection
import org.rajat.quickpick.presentation.feature.vendor.components.VendorNotFound
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorScreen(
    navController: NavController,
    vendorViewModel: VendorViewModel,
    vendorId: String,
    onBackClick: () -> Unit = { navController.navigateUp() }
) {
    val vendorDetailsState by vendorViewModel.vendorsDetailState.collectAsState()
    var selectedCategoryId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(vendorId) {
        vendorViewModel.getVendorsDetails(vendorId)
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

    if (selectedCategoryId != null) {
        MenuItemScreen(
            navController = navController,
            vendorId = vendorId,
            category = selectedCategoryId!!,
            onBackClick = {
                selectedCategoryId = null
            }
        )
        return
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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                val rating = DummyData.getRatingByVendorId(vendor.id ?: "")
                val categories = vendor.foodCategories?.filterNotNull() ?: emptyList()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        VendorHeaderSection(
                            vendor = vendor,
                            rating = rating
                        )
                    }

                    item {
                        OffersSection()
                    }

                    item {
                        Text(
                            text = "Categories",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                        )
                    }

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
                                        selectedCategoryId = rowCategories[0]
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (rowCategories.size > 1) {
                                CategoryCard(
                                    category = rowCategories[1],
                                    onClick = {
                                        selectedCategoryId = rowCategories[1]
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
