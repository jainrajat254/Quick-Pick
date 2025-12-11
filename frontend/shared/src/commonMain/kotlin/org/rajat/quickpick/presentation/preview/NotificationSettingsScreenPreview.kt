package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.profile.NotificationSettingsScreen
import org.rajat.quickpick.presentation.theme.AppTheme

// --- PREVIEWS ---
@Preview(showBackground = true, name = "Notification Settings Light")
@Composable
fun NotificationSettingsScreenLightPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NotificationSettingsScreen(
                paddingValues = PaddingValues(0.dp),
                navController = rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true, name = "Notification Settings Dark")
@Composable
fun NotificationSettingsScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            NotificationSettingsScreen(
                paddingValues = PaddingValues(0.dp),
                navController = rememberNavController()
            )
        }
    }
}
