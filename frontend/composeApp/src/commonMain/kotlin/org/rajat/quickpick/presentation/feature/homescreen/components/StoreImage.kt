package org.rajat.quickpick.presentation.feature.homescreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

            Box(
                modifier = Modifier.fillMaxSize().background(Color.Gray)
            ){
                //bring image.
            }
        } else {
            Image(
                painter = painterResource(
                    resource = Res.drawable.storeimage
                ),
                contentDescription = "Store Image",
                modifier = Modifier.clip(
                    MaterialTheme.shapes.medium
                )

            )
        }
    }
}