package org.rajat.quickpick.presentation.feature.myorders.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingSelector(
    maxRating: Int = 5,
    currentRating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..maxRating) {
            val isSelected = i <= currentRating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Filled.Star
            val tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            Icon(
                imageVector = icon,
                contentDescription = "$i star",
                tint = tint,
                modifier = Modifier
                    .size(40.dp) // Adjust star size
                    .clickable { onRatingChange(i) }
                    .padding(4.dp)
            )
        }
    }
}