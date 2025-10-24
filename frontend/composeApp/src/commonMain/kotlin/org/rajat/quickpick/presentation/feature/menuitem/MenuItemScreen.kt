package org.rajat.quickpick.presentation.feature.menuitem

import androidx.compose.runtime.Composable
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
import org.rajat.quickpick.presentation.feature.menuitem.components.MenuItemCard
import org.rajat.quickpick.presentation.feature.menuitem.components.MenuItemEmptyState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemScreen(
    navController: NavController? = null,
    vendorId: String,
    category: String,
    onBackClick: () -> Unit = { navController?.navigateUp() }
) {
    val menuItems = remember(vendorId, category) {
        DummyData.getMenuItemsByVendorIdAndCategory(vendorId, category)
    }

    val vendor = remember(vendorId) {
        DummyData.vendors.find { it.id == vendorId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        vendor?.storeName?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
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
                    key = { it.id ?: "unknown" }
                ) { menuItem ->
                    MenuItemCard(
                        menuItem = menuItem,
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