package org.rajat.quickpick.presentation.feature.auth.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingProgressBar(
    modifier: Modifier = Modifier,
    progressStep: Int,
    totalSteps: Int
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        val filledWidth = (progressStep.toFloat() / totalSteps.toFloat())
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(filledWidth)
                .background(MaterialTheme.colorScheme.primary)
        )
    }
}