package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.profile.ChangePasswordScreen
import org.rajat.quickpick.presentation.theme.AppTheme


//PREVIEWS
@Preview(showBackground = true, name = "Change Password Light")
@Composable
fun ChangePasswordScreenLightPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ChangePasswordScreen(
                paddingValues = PaddingValues(0.dp),
                isLoading = false,
                navController = rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true, name = "Change Password Dark")
@Composable
fun ChangePasswordScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ChangePasswordScreen(
                paddingValues = PaddingValues(0.dp),
                isLoading = false,
                navController = rememberNavController()
            )
        }
    }
}