package org.rajat.quickpick.presentation.feature.home


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.data.dummy.DummyData
import org.rajat.quickpick.presentation.components.BasePage
import org.rajat.quickpick.presentation.feature.home.components.*
import org.rajat.quickpick.presentation.feature.vendor.VendorScreen


@Composable
fun HomeScreen(
    navController: NavController,
    paddingValues: PaddingValues
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedVendorId by remember { mutableStateOf<String?>(null) }

    val allVendors = remember { DummyData.vendors }

    val filteredVendors = remember(searchQuery, allVendors) {
        if (searchQuery.isBlank()) {
            allVendors
        } else {
            allVendors.filter { vendor ->
                (vendor.storeName?.contains(searchQuery, ignoreCase = true) == true) ||
                        (vendor.vendorName?.contains(searchQuery, ignoreCase = true) == true) ||
                        (vendor.foodCategories?.any {
                            it?.contains(
                                searchQuery,
                                ignoreCase = true
                            ) == true
                        } == true)
            }
        }
    }

    // If a vendor is selected, show VendorScreen
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

        if (filteredVendors.isEmpty()) {
            EmptyState(
                searchQuery = searchQuery,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            VendorsList(
                vendors = filteredVendors,
                onVendorClick = { vendorId ->
                    selectedVendorId = vendorId
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
