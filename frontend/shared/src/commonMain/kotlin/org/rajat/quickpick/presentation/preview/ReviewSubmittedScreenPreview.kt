package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.myorders.ReviewOrderConfirmationScreen
import org.rajat.quickpick.presentation.theme.AppTheme


// --- PREVIEWS ---
@Preview(showBackground = true, name = "Review Confirm Light")
@Composable
fun ReviewSubmittedScreenLightPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReviewOrderConfirmationScreen(
                navController = rememberNavController(),
                PaddingValues(0.dp)
            )
        }
    }
}

@Preview(showBackground = true, name = "Review Confirm Dark")
@Composable
fun ReviewSubmittedScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ReviewOrderConfirmationScreen(
                navController = rememberNavController(),
                PaddingValues(0.dp)
            )
        }
    }
}
