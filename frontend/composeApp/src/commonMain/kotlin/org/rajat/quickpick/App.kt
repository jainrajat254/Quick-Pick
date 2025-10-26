package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.home.HomeScreen
import org.rajat.quickpick.presentation.theme.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        val navHostController = rememberNavController()
        AppNavigation(
            navController = navHostController
        )
    }
}