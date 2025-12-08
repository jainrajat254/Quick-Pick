package org.rajat.quickpick.presentation.feature.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import org.jetbrains.compose.resources.painterResource
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.storeimage

@Composable
fun StoreImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(
                color = MaterialTheme.colorScheme.background
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
            CoilImage(
                imageModel = { imageUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
                previewPlaceholder = painterResource(resource = Res.drawable.storeimage),
                failure = {
                    // Show default image if loading fails
                    Image(
                        painter = painterResource(resource = Res.drawable.storeimage),
                        contentDescription = "Store Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            )
        } else {
            Image(
                painter = painterResource(
                    resource = Res.drawable.storeimage
                ),
                contentDescription = "Store Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }
    }
}