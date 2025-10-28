package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.data.dummy.profile
import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.presentation.feature.profile.MyProfileScreen
import org.rajat.quickpick.presentation.theme.AppTheme

//PREVIEWS
@Preview(showBackground = true, name = "My Profile Light")
@Composable
fun MyProfileScreenLightPreview() {

    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MyProfileScreen(
                paddingValues = PaddingValues(0.dp),
                profile = profile,
                isLoading = false,
                navController = rememberNavController()
            )
        }
    }
}


@Preview(showBackground = true, name = "My Profile Dark")
@Composable
fun MyProfileScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MyProfileScreen(
                paddingValues = PaddingValues(0.dp),
                profile = profile,
                isLoading = false,
                navController = rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true, name = "My Profile Loading")
@Composable
fun MyProfileScreenLoadingPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MyProfileScreen(
                paddingValues = PaddingValues(0.dp),
                profile = null,
                isLoading = true,
                navController = rememberNavController()
            )
        }
    }
}

