package org.rajat.quickpick.presentation.feature.vendor.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
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
    val deleteState by menuItemViewModel.deleteMenuItemState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { menuItemViewModel.getMyMenuItems(page = 0, size = 100) }

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

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is UiState.Success -> {
                showToast("Item deleted")
                menuItemViewModel.resetDeleteMenuItemState()
                menuItemViewModel.getMyMenuItems(page = 0, size = 100)
                isDeleting = false
            }
            is UiState.Error -> {
                showToast((deleteState as UiState.Error).message ?: "Failed to delete item")
                menuItemViewModel.resetDeleteMenuItemState()
                isDeleting = false
            }
            is UiState.Loading -> isDeleting = true
            UiState.Empty -> Unit
        }
    }

    fun requestDelete(id: String) {
        pendingDeleteId = id
        showDeleteDialog = true
    }

    fun performDelete() {
        pendingDeleteId?.let { menuItemViewModel.deleteMenuItem(it) }
        showDeleteDialog = false
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
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
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
                        is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CustomLoader() }
                        is UiState.Success -> {
                            val items = (searchedMenuItemsState as UiState.Success).data.content?.filterNotNull() ?: emptyList()
                            if (items.isEmpty()) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No items found", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                                                items[index].id?.let { id -> menuItemViewModel.toggleMenuItemAvailability(id) }
                                            },
                                            onEdit = { items[index].id?.let { id -> navController.navigate(AppScreenVendor.UpdateMenuItemScreen(id)) } },
                                            onDelete = { items[index].id?.let { id -> requestDelete(id) } }
                                        )
                                    }
                                }
                            }
                        }
                        is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text((searchedMenuItemsState as UiState.Error).message ?: "Error loading items", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                                Button(onClick = { menuItemViewModel.searchMenuItems(query = searchQuery) }) { Text("Retry") }
                            }
                        }
                        UiState.Empty -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CustomLoader() }
                    }
                }
                else -> {
                    when (myMenuItemsState) {
                        is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CustomLoader() }
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
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Icon(imageVector = Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("No ${tabs[selectedTab].lowercase()} menu items", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        if (allItems.isEmpty()) {
                                            Text("Add your first menu item using the + button", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                                            onToggleAvailability = { filteredItems[index].id?.let { id -> menuItemViewModel.toggleMenuItemAvailability(id) } },
                                            onEdit = { filteredItems[index].id?.let { id -> navController.navigate(AppScreenVendor.UpdateMenuItemScreen(id)) } },
                                            onDelete = { filteredItems[index].id?.let { id -> requestDelete(id) } }
                                        )
                                    }
                                }
                            }
                        }
                        is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text((myMenuItemsState as UiState.Error).message ?: "Error loading menu", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.error)
                                Button(onClick = { menuItemViewModel.getMyMenuItems(page = 0, size = 100) }) { Text("Retry") }
                            }
                        }
                        UiState.Empty -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CustomLoader() }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { navController.navigate(AppScreenVendor.AddMenuItemScreen) },
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) { Icon(imageVector = Icons.Default.Add, contentDescription = "Add Menu Item") }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Menu Item") },
            text = { Text("Are you sure you want to delete this item? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { performDelete() }, enabled = !isDeleting) {
                    if (isDeleting) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp))
                    } else {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") } }
        )
    }
}
