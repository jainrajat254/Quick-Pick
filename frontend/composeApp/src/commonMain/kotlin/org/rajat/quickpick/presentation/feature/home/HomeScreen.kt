package org.rajat.quickpick.presentation.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.viewmodel.resolveViewModel
import org.rajat.quickpick.domain.modal.search.GetAllVendorsInCollegeResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.ErrorState
import org.rajat.quickpick.presentation.feature.home.components.EmptyState
import org.rajat.quickpick.presentation.feature.home.components.SearchBar
import org.rajat.quickpick.presentation.feature.home.components.VendorsList
import org.rajat.quickpick.presentation.feature.vendor.VendorScreen
import org.rajat.quickpick.presentation.viewmodel.HomeViewModel
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.presentation.viewmodel.ReviewViewModel
import org.rajat.quickpick.presentation.viewmodel.VendorViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel,
    vendorViewModel: VendorViewModel,
    reviewViewModel: ReviewViewModel,
    menuItemViewModel: MenuItemViewModel
) {
    var searchQuery by remember { mutableStateOf("") }

    val vendorsState by homeViewModel.vendorsInCollegeState.collectAsState()
    val selectedVendorId by homeViewModel.selectedVendorId.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.getVendorsInCollege()
    }

    LaunchedEffect(vendorsState) {
        when (vendorsState) {
            is UiState.Error -> {
                val message = (vendorsState as UiState.Error).message ?: "Failed to load vendors"
                showToast(message)
            }
            else -> Unit
        }
    }

    if (selectedVendorId != null) {
        VendorScreen(
            navController = navController,
            vendorViewModel = vendorViewModel,
            menuItemViewModel = menuItemViewModel,
            reviewViewModel = reviewViewModel,
            vendorId = selectedVendorId!!,
            onBackClick = {
                homeViewModel.setSelectedVendorId(null)
            }
        )
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )

        when (vendorsState) {
            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CustomLoader()
                }
            }

            UiState.Empty -> {
                EmptyState(
                    searchQuery = searchQuery,
                    modifier = Modifier.fillMaxSize()
                )
            }

            is UiState.Error -> {
                ErrorState(
                    message = (vendorsState as UiState.Error).message ?: "Failed to load vendors",
                    modifier = Modifier.fillMaxSize()
                )
            }

            is UiState.Success -> {
                val vendors = (vendorsState as UiState.Success<GetAllVendorsInCollegeResponse>).data

                VendorsList(
                    vendors = vendors.vendors,
                    onVendorClick = { vendorId ->
                        homeViewModel.setSelectedVendorId(vendorId)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
