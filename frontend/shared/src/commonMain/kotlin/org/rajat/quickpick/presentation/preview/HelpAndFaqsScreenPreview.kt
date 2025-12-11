package org.rajat.quickpick.presentation.preview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.profile.HelpAndFaqsScreen
import org.rajat.quickpick.presentation.theme.AppTheme


//PREVIEWS
@Preview(showBackground = true, name = "Help & FAQs Light")
@Composable
fun HelpAndFaqsScreenLightPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HelpAndFaqsScreen(
                paddingValues = PaddingValues(0.dp),
                rememberNavController()
            )
        }
    }
}

@Preview(showBackground = true, name = "Help & FAQs Dark")
@Composable
fun HelpAndFaqsScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            HelpAndFaqsScreen(
                paddingValues = PaddingValues(0.dp),
                rememberNavController()
            )
        }
    }
}