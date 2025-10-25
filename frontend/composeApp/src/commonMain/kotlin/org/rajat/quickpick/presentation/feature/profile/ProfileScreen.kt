package org.rajat.quickpick.presentation.feature.profile
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dummyproject.screens.profile.components.ProfileHeader
import com.example.dummyproject.screens.profile.components.ProfileMenuItem
import com.example.dummyproject.theme.AppTheme

@Composable
fun ProfileScreen(paddingValues: PaddingValues,
                  onClickMyProfile :() -> Unit,
                  onClickPaymentMethods : () -> Unit,
                  onClickMyReviews : () ->  Unit,
                  onClickContactUs : () ->  Unit,
                  onClickHelpFAQs : () ->  Unit,
                  onClickSettings : () ->  Unit,
                  onClickLogOut : () ->  Unit
) {
    val userName = "John Smith"
    val userEmail = "Loremipsum@email.com"
    val profileUrl= ""


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ProfileHeader(
            userName = userName,
            userEmail = userEmail,
            profileUrl = profileUrl
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ProfileMenuItem(
                    text = "My Profile",
                    icon = Icons.Default.Person,
                    onClick = {
                        onClickMyProfile()
                    }
                )
            }
            item {
                ProfileMenuItem(
                    text = "Payment Methods",
                    icon = Icons.Default.ShoppingCart,
                    onClick = { /* TODO: Handle click */ }
                )
            }
            item {
                ProfileMenuItem(
                    text = "My Reviews",
                    icon = Icons.Default.Star,
                    onClick = { /* TODO: Handle click */ }
                )
            }
            item {
                ProfileMenuItem(
                    text = "Contact Us",
                    icon = Icons.Outlined.Call,
                    onClick = { /* TODO: Handle click */ }
                )
            }
            item {
                ProfileMenuItem(
                    text = "Help & FAQs",
                    icon = Icons.Default.Info,
                    onClick = { /* TODO: Handle click */ }
                )
            }
            item {
                ProfileMenuItem(
                    text = "Settings",
                    icon = Icons.Default.Settings,
                    onClick = { /* TODO: Handle click */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuItem(
            text = "Log Out",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = { /* TODO: Handle click */ },
            isLogout = true
        )
    }
}
// Preview for Profile Screen
@Preview(showBackground = true, name = "Profile Screen Light")
@Composable
fun ProfileScreenLightPreview() {
    AppTheme(darkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ProfileScreen(
                paddingValues = PaddingValues(0.dp),
                onClickMyProfile = {},
                onClickPaymentMethods = {},
                onClickMyReviews = {},
                onClickContactUs = {},
                onClickHelpFAQs = {},
                onClickSettings = {},
                onClickLogOut = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Profile Screen Dark")
@Composable
fun ProfileScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ProfileScreen(paddingValues = PaddingValues(0.dp),
                onClickMyProfile = {},
                onClickPaymentMethods = {},
                onClickMyReviews = {},
                onClickContactUs = {},
                onClickHelpFAQs = {},
                onClickSettings = {},
                onClickLogOut = {},
                )
        }
    }
}
