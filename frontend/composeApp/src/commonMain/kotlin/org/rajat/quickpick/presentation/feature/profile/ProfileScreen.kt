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
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rajat.quickpick.presentation.feature.profile.components.LogoutConfirmationDialog
import org.rajat.quickpick.presentation.feature.profile.components.ProfileHeader
import org.rajat.quickpick.presentation.feature.profile.components.ProfileMenuItem
import org.rajat.quickpick.presentation.navigation.Routes

@Composable
fun ProfileScreen(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    val userName = "John Smith"
    val userEmail = "Loremipsum@email.com"
    val profileUrl = ""

    var showLogoutDialog by remember { mutableStateOf(false) }

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
                        navController.navigate(Routes.MyProfile.route)
                    }
                )
            }
            item {
                ProfileMenuItem(
                    text = "Payment Methods",
                    icon = Icons.Default.ShoppingCart,
                    onClick = {

                    }
                )
            }
            item {
                ProfileMenuItem(
                    text = "Contact Us",
                    icon = Icons.Outlined.Call,
                    onClick = {
                        navController.navigate(Routes.ContactUs.route)
                    }
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
                    onClick = {
                        navController.navigate(Routes.Settings.route)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ProfileMenuItem(
            text = "Log Out",
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = {
                // --- SHOW THE DIALOG ---
                showLogoutDialog = true
            },
            isLogout = true
        )
    }

    //DIALOG COMPOSABLE
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = {
                showLogoutDialog = false // Hide dialog when dismissed
            },
            onConfirmLogout = {
                showLogoutDialog = false
//                navController.navigate(Routes.Welcome.route)
                //Logout logic here
            }
        )
    }
}