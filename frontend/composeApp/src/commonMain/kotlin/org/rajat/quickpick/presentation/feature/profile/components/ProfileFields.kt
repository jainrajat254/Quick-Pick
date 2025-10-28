package org.rajat.quickpick.presentation.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import org.rajat.quickpick.utils.toast.showToast
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.delivery

@Composable
fun MyProfileFields(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    department: String,
    onDepartmentChange: (String) -> Unit,
    profile: GetStudentProfileResponse,
    isLoading: Boolean,
) {
    Box(
        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
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


    ProfileInfoField(
        label = "Full Name",
        value = fullName,
        onValueChange = onFullNameChange,
        icon = Icons.Default.Person,
        isEditable = true
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
        isEditable = true,
        keyboardType = KeyboardType.Phone
    )
    ProfileInfoField(
        label = "Department",
        value = department,
        onValueChange = onDepartmentChange,
        icon = Icons.Default.DateRange,
        isEditable = true
    )
    ProfileInfoField(
        label = "College Name",
        value = profile.collegeName ?: "N/A",
        icon = Icons.Default.AddCircle,
        isEditable = false
    )

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = {
            val updateRequest = UpdateUserProfileRequest(
                fullName = fullName,
                phone = phone,
                department = department,
                profileImageUrl = profile.profileImageUrl
            )

            showToast(
                message = "Profile Updated Successfully",
            )
            //Viewmodel method to update the profile using updateRequest
            // e.g., viewModel.updateProfile(updateRequest)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = isLoading.not(),
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text("Save Changes", fontWeight = FontWeight.Bold)
    }
}