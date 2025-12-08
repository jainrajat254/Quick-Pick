package org.rajat.quickpick.presentation.feature.vendor.menu.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.utils.ImageUploadState
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.delivery

@Composable
fun MenuItemImagePicker(
    imageUri: Any?,
    imageUploadState: ImageUploadState,
    onImagePickerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd
        ) {
            CoilImage(
                imageModel = { imageUri },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                previewPlaceholder = painterResource(resource = Res.drawable.delivery)
            )

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
                                contentDescription = "Upload Image",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        if (imageUploadState is ImageUploadState.Success) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "✓ Image uploaded successfully",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}