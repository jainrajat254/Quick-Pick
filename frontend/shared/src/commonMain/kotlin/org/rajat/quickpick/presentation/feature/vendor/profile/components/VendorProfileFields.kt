package org.rajat.quickpick.presentation.feature.vendor.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.domain.modal.profile.GetVendorProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateVendorProfileRequest
import org.rajat.quickpick.presentation.feature.vendor.menu.components.ProfileInfoField
import org.rajat.quickpick.utils.ImageUploadState
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.delivery


@Composable
fun VendorProfileFields(
    profile: GetVendorProfileResponse,
    storeName: String,
    onStoreNameChange: (String) -> Unit,
    vendorName: String,
    onVendorNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    vendorDescription: String,
    onVendorDescriptionChange: (String) -> Unit,
    foodCategories: String,
    onFoodCategoriesChange: (String) -> Unit,
    isLoading: Boolean,
    isEditMode: Boolean,
    imageUploadState: ImageUploadState,
    uploadedImageUrl: String?,
    onEditModeChange: (Boolean) -> Unit,
    onImagePickerClick: () -> Unit,
    onSaveProfile: (UpdateVendorProfileRequest) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.padding(vertical = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Show uploaded image or existing profile image
            val displayImageUrl = uploadedImageUrl ?: profile.profileImageUrl

            CoilImage(
                imageModel = { displayImageUrl ?: "" },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop
                ),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                previewPlaceholder = painterResource(resource = Res.drawable.delivery)
            )

            if (isEditMode) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .offset(x = (4).dp, y = (4).dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    when (imageUploadState) {
                        is ImageUploadState.Uploading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        else -> {
                            IconButton(
                                onClick = onImagePickerClick,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit Profile Picture",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Show upload success message
        if (imageUploadState is ImageUploadState.Success && isEditMode) {
            Text(
                text = "Image uploaded successfully!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (!isEditMode) {
            Text(
                text = profile.storeName ?: "Store Name",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = profile.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isEditMode) 2.dp else 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (isEditMode) {
                Text(
                    text = "Edit Information",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            ProfileInfoField(
                label = "Store Name",
                value = storeName,
                onValueChange = onStoreNameChange,
                icon = Icons.Default.Business,
                isEditable = isEditMode
            )
            ProfileInfoField(
                label = "Vendor Name (Owner)",
                value = vendorName,
                onValueChange = onVendorNameChange,
                icon = Icons.Default.Person,
                isEditable = isEditMode
            )
            ProfileInfoField(
                label = "Email",
                value = profile.email ?: "N/A",
                icon = Icons.Default.Email,
                isEditable = false
            )
            ProfileInfoField(
                label = "Phone",
                value = phone,
                onValueChange = onPhoneChange,
                icon = Icons.Default.Phone,
                isEditable = isEditMode,
                keyboardType = KeyboardType.Phone
            )
            ProfileInfoField(
                label = "Store Address",
                value = address,
                onValueChange = onAddressChange,
                icon = Icons.Default.Home,
                isEditable = isEditMode,
                singleLine = false,
                maxLines = 3
            )
            ProfileInfoField(
                label = "Store Description",
                value = vendorDescription,
                onValueChange = onVendorDescriptionChange,
                icon = Icons.Default.Info,
                isEditable = isEditMode,
                singleLine = false,
                maxLines = 5
            )
            ProfileInfoField(
                label = "Food Categories",
                value = foodCategories,
                onValueChange = onFoodCategoriesChange,
                icon = Icons.Default.Restaurant,
                isEditable = isEditMode,
                placeholder = "e.g. Pizza, Burger, Indian"
            )
            ProfileInfoField(
                label = "College Name",
                value = profile.collegeName ?: "N/A",
                icon = Icons.Default.School,
                isEditable = false
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (isEditMode) {
        Button(
            onClick = {
                val categoriesList = foodCategories
                    .split(",")
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }

                val request = UpdateVendorProfileRequest(
                    storeName = storeName,
                    vendorName = vendorName,
                    phone = phone,
                    address = address,
                    vendorDescription = vendorDescription,
                    foodCategories = categoriesList,
                    profileImageUrl = uploadedImageUrl ?: profile.profileImageUrl
                )
                onSaveProfile(request)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = isLoading.not() && imageUploadState !is ImageUploadState.Uploading,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if (isEditMode) {
            OutlinedButton(
                onClick = { onEditModeChange(false) },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel", fontWeight = FontWeight.Bold)
            }
        } else {
            Button(
                onClick = { onEditModeChange(true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile", fontWeight = FontWeight.Bold)
            }
        }
    }
}