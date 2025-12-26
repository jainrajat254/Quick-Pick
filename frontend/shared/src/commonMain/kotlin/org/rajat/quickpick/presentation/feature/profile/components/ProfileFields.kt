package org.rajat.quickpick.presentation.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.domain.modal.profile.UpdateUserProfileRequest
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.delivery

@Composable
fun MyProfileFields(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    profile: GetStudentProfileResponse,
    isLoading: Boolean,
    isEditMode: Boolean,
    onEditModeChange: (Boolean) -> Unit,
    onSaveProfile: (UpdateUserProfileRequest) -> Unit
) {
    // Profile picture section
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.padding(vertical = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            CoilImage(
                imageModel = { profile.profileImageUrl ?: "" },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop
                ),
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                previewPlaceholder = painterResource(resource = Res.drawable.delivery)
            )

            // Only show edit icon button when in edit mode
            if (isEditMode) {
                IconButton(
                    onClick = {
                        //Logic to change the profile picture
                    },
                    modifier = Modifier
                        .size(36.dp)
                        .offset(x = (4).dp, y = (4).dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)

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

        // User's name as title (only in view mode)
        if (!isEditMode) {
            Text(
                text = profile.fullName ?: "User Name",
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

    // Profile information section
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
                label = "Full Name",
                value = fullName,
                onValueChange = onFullNameChange,
                icon = Icons.Default.Person,
                isEditable = isEditMode
            )

            if (isEditMode) {
                ProfileInfoField(
                    label = "Email",
                    value = profile.email ?: "N/A",
                    icon = Icons.Default.Email,
                    isEditable = false
                )
            }

            ProfileInfoField(
                label = "Phone",
                value = phone,
                onValueChange = onPhoneChange,
                icon = Icons.Default.Phone,
                isEditable = isEditMode,
                keyboardType = KeyboardType.Phone
            )
            ProfileInfoField(
                label = "College Name",
                value = profile.collegeName ?: "N/A",
                icon = Icons.Default.AddCircle,
                isEditable = false
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Only show Save button when in edit mode
    if (isEditMode) {
        Button(
            onClick = {
                val updateRequest = UpdateUserProfileRequest(
                    fullName = fullName,
                    phone = phone,
                    profileImageUrl = profile.profileImageUrl
                )

                // Call the ViewModel method to update the profile
                onSaveProfile(updateRequest)
                // Exit edit mode after saving
                onEditModeChange(false)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = isLoading.not(),
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

    // Edit/Cancel Button
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.weight(1f))

        if (isEditMode) {
            OutlinedButton(
                onClick = { onEditModeChange(false) },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cancel",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("Cancel")
            }
        } else {
            Button(
                onClick = { onEditModeChange(true) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text("Edit Profile")
            }
        }
    }
}