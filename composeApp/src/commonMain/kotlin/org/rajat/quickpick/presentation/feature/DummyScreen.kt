package org.rajat.quickpick.presentation.feature

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DummyScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Theme Color Showcase",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ColorRow(
                left = ColorCardData(
                    name = "Primary",
                    color = MaterialTheme.colorScheme.primary,
                    onColor = MaterialTheme.colorScheme.onPrimary
                ),
                right = ColorCardData(
                    name = "Primary Container",
                    color = MaterialTheme.colorScheme.primaryContainer,
                    onColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            ColorRow(
                left = ColorCardData(
                    name = "Secondary",
                    color = MaterialTheme.colorScheme.secondary,
                    onColor = MaterialTheme.colorScheme.onSecondary
                ),
                right = ColorCardData(
                    name = "Secondary Container",
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    onColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            ColorRow(
                left = ColorCardData(
                    name = "Surface",
                    color = MaterialTheme.colorScheme.surface,
                    onColor = MaterialTheme.colorScheme.onSurface
                ),
                right = ColorCardData(
                    name = "Background",
                    color = MaterialTheme.colorScheme.background,
                    onColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

@Composable
private fun ColorRow(left: ColorCardData, right: ColorCardData) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ColorSquareCard(
            colorName = left.name,
            color = left.color,
            onColor = left.onColor,
            modifier = Modifier.weight(1f)
        )
        ColorSquareCard(
            colorName = right.name,
            color = right.color,
            onColor = right.onColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ColorSquareCard(
    colorName: String,
    color: androidx.compose.ui.graphics.Color,
    onColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = colorName,
                color = onColor,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

private data class ColorCardData(
    val name: String,
    val color: androidx.compose.ui.graphics.Color,
    val onColor: androidx.compose.ui.graphics.Color
)