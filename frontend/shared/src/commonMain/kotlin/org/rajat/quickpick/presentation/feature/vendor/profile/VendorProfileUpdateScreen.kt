package org.rajat.quickpick.presentation.feature.vendor.profile


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.profile.GetVendorProfileResponse
import org.rajat.quickpick.presentation.feature.vendor.profile.components.VendorProfileFields
import org.rajat.quickpick.presentation.viewmodel.ProfileViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.ImagePickerHelper
import org.rajat.quickpick.utils.ImageUploadState

@Composable
fun VendorProfileUpdateScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    profileViewModel: ProfileViewModel = koinInject(),
    imagePickerHelper: ImagePickerHelper
) {
    val vendorProfileState by profileViewModel.vendorProfileState.collectAsState()
    val updateProfileState by profileViewModel.updateVendorProfileState.collectAsState()
    val imageUploadState by profileViewModel.imageUploadState.collectAsState()
    val uploadedImageUrl by profileViewModel.uploadedImageUrl.collectAsState()

    var storeName by remember { mutableStateOf("") }
    var vendorName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var vendorDescription by remember { mutableStateOf("") }
    var foodCategories by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }
    var currentProfile by remember { mutableStateOf<GetVendorProfileResponse?>(null) }

    LaunchedEffect(Unit) {
        profileViewModel.getVendorProfile()
    }

    LaunchedEffect(vendorProfileState) {
        when (vendorProfileState) {
            is UiState.Success -> {
                val profile = (vendorProfileState as UiState.Success<GetVendorProfileResponse>).data
                currentProfile = profile

                storeName = profile.storeName ?: ""
                vendorName = profile.vendorName ?: ""
                phone = profile.phone ?: ""
                address = profile.address ?: ""
                vendorDescription = profile.vendorDescription ?: ""
                foodCategories = profile.foodCategories?.filterNotNull()?.joinToString(", ") ?: ""
            }
            is UiState.Error -> {
                showToast("Failed to load profile: ${(vendorProfileState as UiState.Error).message}")
            }
            else -> {}
        }
    }

    LaunchedEffect(updateProfileState) {
        when (updateProfileState) {
            is UiState.Success -> {
                showToast("Profile updated successfully")
                isEditMode = false
                profileViewModel.clearUploadedImage()
                profileViewModel.getVendorProfile()
                profileViewModel.resetProfileStates()
            }
            is UiState.Error -> {
                showToast("Failed to update profile: ${(updateProfileState as UiState.Error).message}")
                profileViewModel.resetProfileStates()
            }
            else -> {}
        }
    }

    // Handle image upload errors
    LaunchedEffect(imageUploadState) {
        if (imageUploadState is ImageUploadState.Error) {
            showToast("Image upload failed: ${(imageUploadState as ImageUploadState.Error).message}")
        }
    }

    val isLoading = vendorProfileState is UiState.Loading || updateProfileState is UiState.Loading

    DisposableEffect(Unit) {
        onDispose {
            if (isEditMode) {
                profileViewModel.clearUploadedImage()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading && currentProfile == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                currentProfile == null && vendorProfileState is UiState.Error -> {
                    Text(
                        "Could not load profile.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { profileViewModel.getVendorProfile() }) {
                        Text("Retry")
                    }
                }
                currentProfile != null -> {
                    VendorProfileFields(
                        profile = currentProfile!!,
                        storeName = storeName,
                        onStoreNameChange = { storeName = it },
                        vendorName = vendorName,
                        onVendorNameChange = { vendorName = it },
                        phone = phone,
                        onPhoneChange = { phone = it },
                        address = address,
                        onAddressChange = { address = it },
                        vendorDescription = vendorDescription,
                        onVendorDescriptionChange = { vendorDescription = it },
                        foodCategories = foodCategories,
                        onFoodCategoriesChange = { foodCategories = it },
                        isLoading = isLoading,
                        isEditMode = isEditMode,
                        imageUploadState = imageUploadState,
                        uploadedImageUrl = uploadedImageUrl,
                        onEditModeChange = {
                            isEditMode = it
                            if (!it) {
                                storeName = currentProfile?.storeName ?: ""
                                vendorName = currentProfile?.vendorName ?: ""
                                phone = currentProfile?.phone ?: ""
                                address = currentProfile?.address ?: ""
                                vendorDescription = currentProfile?.vendorDescription ?: ""
                                foodCategories = currentProfile?.foodCategories
                                    ?.filterNotNull()?.joinToString(", ") ?: ""
                                profileViewModel.clearUploadedImage()
                            }
                        },
                        onImagePickerClick = {
                            imagePickerHelper.pickImage(
                                onImageSelected = { imageData ->
                                    profileViewModel.uploadProfileImage(
                                        imageBytes = imageData.bytes,
                                        fileName = imageData.fileName
                                    )
                                },
                                onError = { error ->
                                    showToast("Failed to pick image: $error")
                                }
                            )
                        },
                        onSaveProfile = { request ->
                            val updatedRequest = request.copy(
                                profileImageUrl = uploadedImageUrl ?: currentProfile?.profileImageUrl
                            )
                            profileViewModel.updateVendorProfile(updatedRequest)
                        }
                    )
                }
            }
        }
    }
}