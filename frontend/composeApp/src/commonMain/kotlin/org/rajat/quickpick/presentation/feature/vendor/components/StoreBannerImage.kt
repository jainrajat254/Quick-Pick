package org.rajat.quickpick.presentation.feature.vendor.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.storeimage

@Composable
fun StoreBannerImage(
    imageUrl: String?,
    storeName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (!imageUrl.isNullOrBlank()) {
        } else {
            Image(
                painter = painterResource(Res.drawable.storeimage),
                contentDescription = storeName,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(0.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}