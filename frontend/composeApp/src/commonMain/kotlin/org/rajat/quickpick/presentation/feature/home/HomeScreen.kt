package org.rajat.quickpick.presentation.feature.home

import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.util.Logger
import org.rajat.quickpick.domain.modal.search.GetAllVendorsInCollegeResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.feature.home.components.*
import org.rajat.quickpick.presentation.feature.vendor.VendorScreen
import org.rajat.quickpick.presentation.viewmodel.HomeViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    homeViewModel: HomeViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedVendorId by remember { mutableStateOf<String?>(null) }

    val logger = co.touchlab.kermit.Logger.withTag("HOME_SCREEN")

    val vendorsState by homeViewModel.vendorsInCollegeState.collectAsState()

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
            vendorId = selectedVendorId!!,
            onBackClick = { selectedVendorId = null }
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

        when(vendorsState){
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
                    vendors = vendors.filterNotNull(),
                    onVendorClick = { vendorId ->
                        selectedVendorId = vendorId
                    },
                    modifier = Modifier.fillMaxSize()

                )
            }
        }
    }
}
