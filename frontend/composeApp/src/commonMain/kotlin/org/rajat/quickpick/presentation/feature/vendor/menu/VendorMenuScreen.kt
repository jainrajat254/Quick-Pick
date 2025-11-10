package org.rajat.quickpick.presentation.feature.vendor.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.feature.vendor.menu.components.VendorMenuItemCard
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun VendorMenuScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    menuItemViewModel: MenuItemViewModel = koinInject()
) {
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val tabs = listOf("All Items", "Available", "Unavailable")

    val myMenuItemsState by menuItemViewModel.myMenuItemsState.collectAsState()
    val searchedMenuItemsState by menuItemViewModel.searchedMenuItemsState.collectAsState()
    val toggleAvailabilityState by menuItemViewModel.toggleAvailabilityState.collectAsState()

    LaunchedEffect(Unit) {
        menuItemViewModel.getMyMenuItems(page = 0, size = 100)
    }

    LaunchedEffect(toggleAvailabilityState) {
        when (toggleAvailabilityState) {
            is UiState.Success -> {
                showToast("Availability updated successfully")
                menuItemViewModel.resetToggleAvailabilityState()
                menuItemViewModel.getMyMenuItems(page = 0, size = 100)
            }
            is UiState.Error -> {
                showToast((toggleAvailabilityState as UiState.Error).message ?: "Error updating availability")
                menuItemViewModel.resetToggleAvailabilityState()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.isNotBlank()) {
                    menuItemViewModel.searchMenuItems(query = it, availableOnly = false)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search menu items...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            singleLine = true,
            shape = MaterialTheme.shapes.medium
        )

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

        Box(modifier = Modifier.weight(1f)) {
            when {
                searchQuery.isNotBlank() -> {
                    when (searchedMenuItemsState) {
                        is UiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomLoader()
                            }
                        }
                        is UiState.Success -> {
                            val items = (searchedMenuItemsState as UiState.Success).data.content?.filterNotNull() ?: emptyList()
                            if (items.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No items found",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(items.size) { index ->
                                        VendorMenuItemCard(
                                            menuItem = items[index],
                                            onToggleAvailability = {
                                                items[index].id?.let { id ->
                                                    menuItemViewModel.toggleMenuItemAvailability(id)
                                                }
                                            },
                                            onEdit = {
                                                items[index].id?.let { id ->
                                                    // TODO: Navigate to edit screen
                                                    showToast("Edit functionality coming soon")
                                                }
                                            },
                                            onDelete = {
                                                items[index].id?.let { id ->
                                                    // TODO: Implement delete
                                                    showToast("Delete functionality coming soon")
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        is UiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = (searchedMenuItemsState as UiState.Error).message ?: "Error loading items",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Button(
                                        onClick = {
                                            menuItemViewModel.searchMenuItems(query = searchQuery)
                                        }
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                        UiState.Empty -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomLoader()
                            }
                        }
                    }
                }
                else -> {
                    when (myMenuItemsState) {
                        is UiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomLoader()
                            }
                        }
                        is UiState.Success -> {
                            val response = (myMenuItemsState as UiState.Success).data
                            val allItems = response.content?.filterNotNull() ?: emptyList()

                            val filteredItems = when (selectedTab) {
                                0 -> allItems
                                1 -> allItems.filter { it.available == true }
                                2 -> allItems.filter { it.available != true }
                                else -> allItems
                            }

                            if (filteredItems.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                            modifier = Modifier.size(64.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "No ${tabs[selectedTab].lowercase()} menu items",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        if (allItems.isEmpty()) {
                                            Text(
                                                text = "Add your first menu item using the + button",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(filteredItems.size) { index ->
                                        VendorMenuItemCard(
                                            menuItem = filteredItems[index],
                                            onToggleAvailability = {
                                                filteredItems[index].id?.let { id ->
                                                    menuItemViewModel.toggleMenuItemAvailability(id)
                                                }
                                            },
                                            onEdit = {
                                                filteredItems[index].id?.let { id ->
                                                    // TODO: Navigate to edit screen
                                                    showToast("Edit functionality coming soon")
                                                }
                                            },
                                            onDelete = {
                                                filteredItems[index].id?.let { id ->
                                                    // TODO: Implement delete
                                                    showToast("Delete functionality coming soon")
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        is UiState.Error -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = (myMenuItemsState as UiState.Error).message ?: "Error loading menu",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Button(
                                        onClick = {
                                            menuItemViewModel.getMyMenuItems(page = 0, size = 100)
                                        }
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                        UiState.Empty -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CustomLoader()
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    // TODO: Navigate to create menu item screen
                    showToast("Add menu item functionality coming soon")
                },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Menu Item"
                )
            }
        }
    }
}
