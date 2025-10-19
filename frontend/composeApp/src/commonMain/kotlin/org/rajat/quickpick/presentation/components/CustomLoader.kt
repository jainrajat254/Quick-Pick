package org.rajat.quickpick.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomLoader(
    modifier: Modifier = Modifier,
    color: Color = Color(0xFFFF9500),
    strokeWidth: Dp = 4.dp,
    label: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = color,
            strokeWidth = strokeWidth
        )
        label?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}