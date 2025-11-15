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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemRequest
import org.rajat.quickpick.domain.modal.menuitems.UpdateMenuItemResponse // Dummy
import org.rajat.quickpick.presentation.feature.vendor.menu.components.MenuItemDetailsForm
import org.rajat.quickpick.presentation.feature.vendor.menu.components.MenuItemImagePicker
import org.rajat.quickpick.presentation.feature.vendor.menu.components.UpdateMenuItemButton
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun UpdateMenuItemScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    menuItemId: String
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var isAvailable by rememberSaveable { mutableStateOf(true) }
    var isVeg by rememberSaveable { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<Any?>(null) }

    var isDeleting by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) } // <--

    var isSaving by remember { mutableStateOf(false) }
    var isPageLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(menuItemId) {
        delay(1000) // Simulating network call
        val dummyItem = UpdateMenuItemResponse(
            id = menuItemId,
            name = "Classic Burger",
            description = "A delicious beef patty with lettuce, tomato, and cheese.",
            price = 9.99,
            quantity = 50,
            category = "Main Course",
            available = true,
            veg = false,
            imageUrl = null,
            createdAt = "",
            updatedAt = "",
            vendorId = ""
        )

        name = dummyItem.name ?: ""
        description = dummyItem.description ?: ""
        price = dummyItem.price.toString()
        category = dummyItem.category ?: ""
        quantity = dummyItem.quantity?.toString() ?: ""
        isAvailable = dummyItem.available ?: true
        isVeg = dummyItem.veg ?: false
        imageUri = dummyItem.imageUrl

        isPageLoading = false
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

        val request = UpdateMenuItemRequest(
            name = name,
            description = description.ifBlank { null },
            price = priceDouble,
            category = category.ifBlank { null },
            quantity = quantity.toIntOrNull(),
            isVeg = isVeg,
            isAvailable = isAvailable,
            imageUrl = null //Replace with uploaded image URL
        )

        coroutineScope.launch {
            isSaving = true
            delay(1500)
            isSaving = false
            showToast("Menu item updated successfully (dummy)")
            navController.popBackStack()
        }
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
                        // TODO: Logic to launch image picker
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
                    onIsAvailableChange = { isAvailable = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                UpdateMenuItemButton(
                    isLoading = isSaving,
                    onClick = ::handleUpdateItemClick
                )
                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = { showDeleteDialog = true },
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
    }
}