package org.rajat.quickpick.presentation.feature.menuitem.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VegNonVegBadge(
    isVeg: Boolean,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isVeg) {
        Color(0xFF4CAF50)
    } else {
        Color(0xFFD93636)
    }

    Box(
        modifier = modifier
            .size(16.dp)
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = MaterialTheme.shapes.extraSmall
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .border(
                    width = 4.dp,
                    color = borderColor,
                    shape = CircleShape
                )
        )
    }
}