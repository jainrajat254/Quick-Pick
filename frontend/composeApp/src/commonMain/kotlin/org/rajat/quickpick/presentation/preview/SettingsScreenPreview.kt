package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.components.BasePage
import org.rajat.quickpick.presentation.feature.profile.SettingsScreen
import org.rajat.quickpick.presentation.feature.profile.components.SettingComponentCard
import org.rajat.quickpick.presentation.theme.AppTheme

@Composable
@Preview
fun SettingsScreenPreview(){
    AppTheme(darkTheme = false){
        BasePage(
            currentRoute = "home",
            onBackClick = {},
            onNavigate ={}
        ){paddingValues ->
            SettingsScreen(
                paddingValues = paddingValues,
                navController = rememberNavController()
            )
        }
    }

}
@Composable
@Preview
fun SettingsScreenPreviewDark(){
    AppTheme(darkTheme = true) {
        BasePage(
            currentRoute = "home",
            onBackClick = {},
            onNavigate = {}
        ) { paddingValues ->
            Surface(modifier = Modifier.fillMaxSize()){
                SettingsScreen(
                    paddingValues = paddingValues,
                    navController = rememberNavController()
                )
            }
        }
    }

}
@Composable
@Preview
fun SettingsScreenComponentPreview(){
    AppTheme(darkTheme = true) {
        SettingComponentCard(
            text = "Notifications",
            icon = Icons.Filled.Notifications,
            onClick = {}
        )
    }
}