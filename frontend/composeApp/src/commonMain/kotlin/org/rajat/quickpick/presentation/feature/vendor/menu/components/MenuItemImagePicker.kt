package org.rajat.quickpick.presentation.feature.vendor.menu.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.painterResource
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.delivery

@Composable
fun MenuItemImagePicker(
    imageUri: Any?,
    onImagePickerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(vertical = 16.dp),
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

        IconButton(
            onClick = onImagePickerClick,
            modifier = Modifier
                .size(36.dp)
                .offset(x = (4).dp, y = (4).dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)

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