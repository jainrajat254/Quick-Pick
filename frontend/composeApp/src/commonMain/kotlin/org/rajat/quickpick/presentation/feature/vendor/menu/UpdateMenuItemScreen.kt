package org.rajat.quickpick.presentation.feature.vendor.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemRequest
import org.rajat.quickpick.presentation.feature.vendor.menu.components.MenuItemDetailsForm
import org.rajat.quickpick.presentation.feature.vendor.menu.components.MenuItemImagePicker
import org.rajat.quickpick.presentation.feature.vendor.menu.components.UpdateMenuItemButton
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.presentation.viewmodel.MenuCategoryViewModel

@Composable
fun UpdateMenuItemScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    menuItemId: String,
    menuItemViewModel: MenuItemViewModel = koinInject(),
    menuCategoryViewModel: MenuCategoryViewModel = koinInject()
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var isAvailable by rememberSaveable { mutableStateOf(true) }
    var isVeg by rememberSaveable { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<Any?>(null) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var pendingDeleteId by remember { mutableStateOf<String?>(null) }

    val singleState by menuItemViewModel.singleMenuItemState.collectAsState()
    val updateState by menuItemViewModel.updateMenuItemState.collectAsState()
    val deleteState by menuItemViewModel.deleteMenuItemState.collectAsState()

    val defaultCategoriesState by menuCategoryViewModel.getDefaultCategoriesState.collectAsState()

    var isSaving by remember { mutableStateOf(false) }
    var isPageLoading by remember { mutableStateOf(true) }
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(menuItemId) {
        menuItemViewModel.getMenuItemById(menuItemId)
        menuCategoryViewModel.getDefaultVendorCategories()
    }

    val categoryOptions: List<String> = when (val state = defaultCategoriesState) {
        is UiState.Success -> state.data.categories
        else -> emptyList()
    }

    LaunchedEffect(singleState) {
        when (singleState) {
            is UiState.Success -> {
                val item = (singleState as UiState.Success).data
                name = item.name ?: ""
                description = item.description ?: ""
                price = item.price?.toString() ?: ""
                category = item.category ?: ""
                quantity = item.quantity?.toString() ?: ""
                isAvailable = item.available ?: true
                isVeg = item.isVeg ?: false
                imageUri = item.imageUrl
                isPageLoading = false
            }
            is UiState.Error -> {
                showToast((singleState as UiState.Error).message ?: "Failed to load item")
                isPageLoading = false
            }
            is UiState.Loading -> {
                isPageLoading = true
            }
            UiState.Empty -> Unit
        }
    }

    LaunchedEffect(updateState) {
        when (updateState) {
            is UiState.Success -> {
                showToast("Menu item updated successfully")
                menuItemViewModel.resetUpdateMenuItemState()
                menuItemViewModel.getMyMenuItems(page = 0, size = 100)
                navController.popBackStack()
            }
            is UiState.Error -> {
                showToast((updateState as UiState.Error).message ?: "Failed to update item")
                isSaving = false
                menuItemViewModel.resetUpdateMenuItemState()
            }
            is UiState.Loading -> isSaving = true
            UiState.Empty -> Unit
        }
    }

    LaunchedEffect(deleteState) {
        when (deleteState) {
            is UiState.Success -> {
                showToast("Menu item deleted")
                menuItemViewModel.resetDeleteMenuItemState()
                menuItemViewModel.getMyMenuItems(page = 0, size = 100)
                navController.popBackStack()
            }
            is UiState.Error -> {
                showToast((deleteState as UiState.Error).message ?: "Failed to delete item")
                isDeleting = false
                menuItemViewModel.resetDeleteMenuItemState()
            }
            is UiState.Loading -> isDeleting = true
            UiState.Empty -> Unit
        }
    }

    fun handleUpdateItemClick() {
        if (name.isBlank() || price.isBlank()) {
            showToast("Item Name and Price are required.")
            return
        }
        val priceDouble = price.toDoubleOrNull()
        if (priceDouble == null || priceDouble <= 0) {
            showToast("Please enter a valid price.")
            return
        }

        if (categoryOptions.isNotEmpty() && category.isBlank()) {
            showToast("Please select a category.")
            return
        }
        if (categoryOptions.isNotEmpty() && category !in categoryOptions) {
            showToast("Please select a valid category from the list.")
            return
        }

        val request = UpdateMenuItemRequest(
            name = name,
            description = description.ifBlank { null },
            price = priceDouble,
            category = category.ifBlank { null },
            quantity = quantity.toIntOrNull(),
            isVeg = isVeg,
            isAvailable = isAvailable,
            imageUrl = null // TODO: Replace with uploaded image URL
        )
        menuItemViewModel.updateMenuItem(menuItemId, request)
    }

    fun confirmDelete(id: String) {
        pendingDeleteId = id
        showDeleteDialog = true
    }

    fun performDelete() {
        pendingDeleteId?.let { menuItemViewModel.deleteMenuItem(it) }
        showDeleteDialog = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        if (isPageLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MenuItemImagePicker(
                    imageUri = imageUri,
                    onImagePickerClick = {
                        // TODO: Integrate image picker and upload
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                MenuItemDetailsForm(
                    name = name,
                    onNameChange = { name = it },
                    description = description,
                    onDescriptionChange = { description = it },
                    price = price,
                    onPriceChange = { price = it },
                    category = category,
                    onCategoryChange = { category = it },
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                    isVeg = isVeg,
                    onIsVegChange = { isVeg = it },
                    isAvailable = isAvailable,
                    onIsAvailableChange = { isAvailable = it },
                    categoryOptions = categoryOptions
                )

                Spacer(modifier = Modifier.height(16.dp))

                UpdateMenuItemButton(
                    isLoading = isSaving,
                    onClick = ::handleUpdateItemClick
                )
                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { confirmDelete(menuItemId) },
                    enabled = !(isSaving || isDeleting)
                ) {
                    Text(
                        "Delete Item",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (isDeleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp).padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Menu Item") },
                text = { Text("Are you sure you want to delete this menu item? This action cannot be undone.") },
                confirmButton = {
                    TextButton(onClick = { performDelete() }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}