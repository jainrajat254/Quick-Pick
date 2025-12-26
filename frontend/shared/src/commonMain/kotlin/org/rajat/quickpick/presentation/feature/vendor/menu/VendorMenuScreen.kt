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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.feature.vendor.menu.components.VendorMenuItemCard
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.utils.BackHandler
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.exitApp
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.ErrorUtils

@OptIn(ExperimentalTime::class)
@Composable
fun VendorMenuScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    menuItemViewModel: MenuItemViewModel = koinInject()
) {
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var backPressedTime by remember { mutableStateOf(0L) }

    val tabs = listOf("All Items", "Available", "Unavailable")

    val myMenuItemsState by menuItemViewModel.myMenuItemsState.collectAsState()
    val searchedMenuItemsState by menuItemViewModel.searchedMenuItemsState.collectAsState()
    val toggleAvailabilityState by menuItemViewModel.toggleAvailabilityState.collectAsState()
    val deleteState by menuItemViewModel.deleteMenuItemState.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }
    var isDeleting by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - backPressedTime < 2000) {
            exitApp()
        } else {
            backPressedTime = currentTime
            showToast("Press back again to exit")
        }
    }

    LaunchedEffect(Unit) { menuItemViewModel.getMyMenuItems(page = 0, size = 100) }

    LaunchedEffect(toggleAvailabilityState) {
        when (toggleAvailabilityState) {
            is UiState.Success -> {
                showToast("Availability updated successfully")
                menuItemViewModel.resetToggleAvailabilityState()
                menuItemViewModel.getMyMenuItems(page = 0, size = 100)
            }
            is UiState.Error -> {
                val raw = (toggleAvailabilityState as UiState.Error).message
                showToast(ErrorUtils.sanitizeError(raw))
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
                val raw = (deleteState as UiState.Error).message
                showToast(ErrorUtils.sanitizeError(raw))
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
//            OutlinedTextField(
//                value = searchQuery,
//                onValueChange = {
//                    searchQuery = it
//                    if (it.isNotBlank()) {
//                        menuItemViewModel.searchMenuItems(query = it, availableOnly = false)
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp, vertical = 8.dp),
//                placeholder = { Text("Search menu items...") },
//                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
//                singleLine = true,
//                shape = MaterialTheme.shapes.medium
//            )

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

            HorizontalDivider(thickness = 1.dp)

            Box(modifier = Modifier.fillMaxSize()) {
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
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp),
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
                                        contentPadding = PaddingValues(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 16.dp,
                                            bottom = 16.dp
                                        ),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                            Button(
                                                onClick = { navController.navigate(AppScreenVendor.AddMenuItemScreen) },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 4.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Create Menu Item")
                                            }
                                        }

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
                            is UiState.Error -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = ErrorUtils.sanitizeError((searchedMenuItemsState as UiState.Error).message),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Button(onClick = { menuItemViewModel.searchMenuItems(query = searchQuery) }) {
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
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp),
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
                                                text = "No items",
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            if (allItems.isEmpty()) {
                                                Text(
                                                    text = "Add your first menu item.",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Button(
                                                    onClick = { navController.navigate(AppScreenVendor.AddMenuItemScreen) },
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = MaterialTheme.colorScheme.primary
                                                    )
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Add,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    Text("Create Menu Item")
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier.fillMaxSize(),
                                        contentPadding = PaddingValues(
                                            start = 16.dp,
                                            end = 16.dp,
                                            top = 16.dp,
                                            bottom = 16.dp
                                        ),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                            Button(
                                                onClick = { navController.navigate(AppScreenVendor.AddMenuItemScreen) },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 4.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primary
                                                )
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Add,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("Create Menu Item")
                                            }
                                        }

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
                            is UiState.Error -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = ErrorUtils.sanitizeError((myMenuItemsState as UiState.Error).message),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Button(onClick = { menuItemViewModel.getMyMenuItems(page = 0, size = 100) }) {
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
