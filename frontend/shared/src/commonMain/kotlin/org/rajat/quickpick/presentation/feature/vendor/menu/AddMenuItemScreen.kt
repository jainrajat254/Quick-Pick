package org.rajat.quickpick.presentation.feature.vendor.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.menuitems.CreateMenuItemRequest
import org.rajat.quickpick.presentation.feature.vendor.menu.components.AddMenuItemButton
import org.rajat.quickpick.presentation.feature.vendor.menu.components.MenuItemDetailsForm
import org.rajat.quickpick.presentation.feature.vendor.menu.components.MenuItemImagePicker
import org.rajat.quickpick.presentation.viewmodel.MenuItemViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.ErrorUtils
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.presentation.viewmodel.MenuCategoryViewModel
import org.rajat.quickpick.utils.ImagePickerHelper
import org.rajat.quickpick.utils.ImageUploadState
import org.rajat.quickpick.presentation.viewmodel.ProfileViewModel
import co.touchlab.kermit.Logger

private val logger = Logger.withTag("CLOUDINARY_IMAGE_DEBUG")

@Composable
fun AddMenuItemScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    menuItemViewModel: MenuItemViewModel = koinInject(),
    menuCategoryViewModel: MenuCategoryViewModel = koinInject(),
    imagePickerHelper: ImagePickerHelper,
    profileViewModel: ProfileViewModel = koinInject()
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
    var uploadedImageUrl by remember { mutableStateOf<String?>(null) }

    val createState by menuItemViewModel.createMenuItemState.collectAsState()
    val defaultCategoriesState by menuCategoryViewModel.getDefaultCategoriesState.collectAsState()
    val imageUploadState by profileViewModel.imageUploadState.collectAsState()

    LaunchedEffect(Unit) {
        menuCategoryViewModel.getDefaultVendorCategories()
    }

    val categoryOptions: List<String> = when (val state = defaultCategoriesState) {
        is UiState.Success -> state.data.categories
        else -> emptyList()
    }

    LaunchedEffect(createState) {
        when (createState) {
            is UiState.Success -> {
                showToast("Menu item added successfully")
                menuItemViewModel.resetCreateMenuItemState()
                menuItemViewModel.getMyMenuItems(page = 0, size = 100)
                navController.popBackStack()
            }
            is UiState.Error -> {
                val raw = (createState as UiState.Error).message
                showToast(ErrorUtils.sanitizeError(raw))
                isLoading = false
                menuItemViewModel.resetCreateMenuItemState()
            }
            is UiState.Loading -> isLoading = true
            UiState.Empty -> Unit
        }
    }

    LaunchedEffect(imageUploadState) {
        logger.d { "AddMenuItemScreen: imageUploadState changed to ${imageUploadState::class.simpleName}" }
        when (imageUploadState) {
            is ImageUploadState.Success -> {
                uploadedImageUrl = (imageUploadState as ImageUploadState.Success).imageUrl
                logger.d { "AddMenuItemScreen: Image upload successful, URL=$uploadedImageUrl" }
                showToast("Image uploaded successfully")
            }
            is ImageUploadState.Error -> {
                val errorMessage = (imageUploadState as ImageUploadState.Error).message
                logger.e { "AddMenuItemScreen: Image upload failed with error=$errorMessage" }
                showToast(ErrorUtils.sanitizeError(errorMessage))
            }
            is ImageUploadState.Uploading -> {
                logger.d { "AddMenuItemScreen: Image is uploading..." }
            }
            is ImageUploadState.Idle -> {
                logger.d { "AddMenuItemScreen: Image upload state is Idle" }
            }
        }
    }

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

        when (defaultCategoriesState) {
            is UiState.Success -> {
                if (category.isBlank()) {
                    showToast("Please select a category.")
                    return
                }
                if (category !in categoryOptions) {
                    showToast("Please select a valid category from the list.")
                    return
                }
            }
            is UiState.Loading, UiState.Empty -> {
                showToast("Loading categories. Please wait...")
                return
            }
            is UiState.Error -> {
                showToast("Couldn't load categories. Pull to refresh and try again.")
                return
            }
        }

        val request = CreateMenuItemRequest(
            name = name,
            description = description.ifBlank { null },
            price = priceDouble,
            category = category.ifBlank { null },
            quantity = quantity.toIntOrNull(),
            isVeg = isVeg,
            isAvailable = isAvailable,
            imageUrl = uploadedImageUrl
        )
        menuItemViewModel.createMenuItem(request)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuItemImagePicker(
            imageUri = imageUri,
            imageUploadState = imageUploadState,
            onImagePickerClick = {
                logger.d { "AddMenuItemScreen: Image picker clicked" }
                imagePickerHelper.pickImage(
                    onImageSelected = { imageData ->
                        logger.d { "AddMenuItemScreen: Image selected - fileName=${imageData.fileName}, size=${imageData.sizeInBytes} bytes" }
                        imageUri = imageData.bytes
                        logger.d { "AddMenuItemScreen: Calling profileViewModel.uploadProfileImage" }
                        profileViewModel.uploadProfileImage(
                            imageBytes = imageData.bytes,
                            fileName = imageData.fileName
                        )
                    },
                    onError = { error ->
                        logger.e { "AddMenuItemScreen: Image picker error=$error" }
                        showToast(ErrorUtils.sanitizeError(error))
                    }
                )
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
        AddMenuItemButton(isLoading = isLoading, onClick = ::handleAddItemClick)
    }
}