package org.rajat.quickpick.presentation.feature.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemRequest
import org.rajat.quickpick.presentation.feature.vendor.menu.components.AddMenuItemButton
import org.rajat.quickpick.presentation.feature.vendor.menu.components.MenuItemDetailsForm
import org.rajat.quickpick.presentation.feature.vendor.menu.components.MenuItemImagePicker
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun AddMenuItemScreen(
    navController: NavController,
    paddingValues: PaddingValues,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var isAvailable by rememberSaveable { mutableStateOf(true) }
    var isVeg by rememberSaveable { mutableStateOf(true) }
    var imageUri by remember { mutableStateOf<Any?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun handleAddItemClick() {
        if (name.isBlank() || price.isBlank()) {
            showToast("Item Name and Price are required.")
            return
        }
        val priceDouble = price.toDoubleOrNull()
        if (priceDouble == null || priceDouble <= 0) {
            showToast("Please enter a valid price.")
            return
        }

        val request = CreateMenuItemRequest(
            name = name,
            description = description.ifBlank { null },
            price = priceDouble,
            category = category.ifBlank { null },
            quantity = quantity.toIntOrNull(),
            isVeg = isVeg,
            isAvailable = isAvailable,
            imageUrl = null
        )

        coroutineScope.launch {
            isLoading = true
            delay(1500)
            isLoading = false
            showToast("Menu item added successfully (dummy)")
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MenuItemImagePicker(
            imageUri = imageUri,
            onImagePickerClick = {
                // Logic to launch image picker
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

        AddMenuItemButton(
            isLoading = isLoading,
            onClick = ::handleAddItemClick
        )
    }
}