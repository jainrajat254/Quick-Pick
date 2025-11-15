package org.rajat.quickpick.presentation.feature.profile

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.domain.modal.auth.LogoutRequest
import org.rajat.quickpick.presentation.feature.profile.components.LogoutConfirmationDialog
import org.rajat.quickpick.presentation.feature.profile.components.ProfileHeader
import org.rajat.quickpick.presentation.feature.profile.components.ProfileMenuItem
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.presentation.viewmodel.ProfileViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun ProfileScreen(
    navController: NavHostController,
    paddingValues: PaddingValues,
    profileViewModel: ProfileViewModel = koinInject(),
    authViewModel: AuthViewModel = koinInject(),
    dataStore: LocalDataStore = koinInject()
) {
    val studentProfileState by profileViewModel.studentProfileState.collectAsState()
    val logoutState by authViewModel.logoutState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var profileUrl by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Fetch profile on first composition
    LaunchedEffect(Unit) {
        profileViewModel.getStudentProfile()
    }

    LaunchedEffect(studentProfileState) {
        when (val state = studentProfileState) {
            is UiState.Success -> {
                val profile = state.data
                userName = profile.fullName ?: "User"
                userEmail = profile.email ?: ""
                profileUrl = profile.profileImageUrl ?: ""
            }
            else -> {}
        }
    }

    LaunchedEffect(logoutState) {
        when (logoutState) {
            is UiState.Success -> {
                // Clear all local data
                dataStore.clearAll()

                // Reset auth states
                authViewModel.resetAuthStates()
                profileViewModel.resetProfileStates()

                showToast("Logged out successfully")

                // Navigate to auth screen
                navController.navigate(AppScreenUser.LaunchWelcome) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is UiState.Error -> {
                // Even if logout API fails, clear local data
                coroutineScope.launch {
                    dataStore.clearAll()
                }
                authViewModel.resetAuthStates()
                profileViewModel.resetProfileStates()

                showToast("Logged out")

                navController.navigate(AppScreenUser.LaunchWelcome) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        when (studentProfileState) {
            is UiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            else -> {
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
                                navController.navigate(AppScreenUser.MyProfile)
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
                                navController.navigate(AppScreenUser.ContactUs)
                            }
                        )
                    }
                    item {
                        ProfileMenuItem(
                            text = "Help & FAQs",
                            icon = Icons.Default.Info,
                            onClick = {
                                navController.navigate(AppScreenUser.HelpAndFaqs)
                            }
                        )
                    }
                    item {
                        ProfileMenuItem(
                            text = "Settings",
                            icon = Icons.Default.Settings,
                            onClick = {
                                navController.navigate(AppScreenUser.Settings)
                            }
                        )
                    }
                    item {
                        ProfileMenuItem(
                            text = "Log Out",
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            onClick = {
                                showLogoutDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false

                // Get refresh token and call logout API
                coroutineScope.launch {
                    val refreshToken = dataStore.getRefreshToken()
                    val logoutRequest = LogoutRequest(refreshToken = refreshToken)
                    authViewModel.logout(logoutRequest)
                }
            }
        )
    }
}