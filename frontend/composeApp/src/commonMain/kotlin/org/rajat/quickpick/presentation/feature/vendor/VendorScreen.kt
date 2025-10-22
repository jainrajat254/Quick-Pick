package org.rajat.quickpick.presentation.feature.vendor


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.data.dummy.DummyData
import org.rajat.quickpick.presentation.feature.vendor.components.CategoryCard
import org.rajat.quickpick.presentation.feature.vendor.components.OffersSection
import org.rajat.quickpick.presentation.feature.vendor.components.VendorHeaderSection
import org.rajat.quickpick.presentation.feature.vendor.components.VendorNotFound


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorScreen(
    navController: NavController,
    vendorId: String,
    onBackClick: () -> Unit = { navController.navigateUp() }
) {

    val vendor = remember(vendorId) {
        DummyData.vendors.find { it.id == vendorId }
    }

    val rating = remember(vendorId) {
        DummyData.getRatingByVendorId(vendorId)
    }

    val categories = remember(vendorId) {
        DummyData.getCategoriesByVendorId(vendorId)
    }

    if (vendor == null) {
        VendorNotFound(
            onBackClick = onBackClick
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = vendor.storeName ?: "Store Details",
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
                                navController.navigate("menu_items/$vendorId/${rowCategories[0]}")
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (rowCategories.size > 1) {
                        CategoryCard(
                            category = rowCategories[1],
                            onClick = {
                                navController.navigate("menu_items/$vendorId/${rowCategories[1]}")
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
