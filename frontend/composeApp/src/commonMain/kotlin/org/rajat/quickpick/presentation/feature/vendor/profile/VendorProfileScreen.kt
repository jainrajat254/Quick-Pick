package org.rajat.quickpick.presentation.feature.vendor.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.koin.compose.koinInject
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.domain.modal.auth.LogoutRequest
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.feature.profile.components.LogoutConfirmationDialog
import org.rajat.quickpick.presentation.feature.vendor.profile.components.VendorProfileOption
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.presentation.viewmodel.ProfileViewModel
import org.rajat.quickpick.utils.BackHandler
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.exitApp
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.tokens.PlatformScheduler

@OptIn(ExperimentalTime::class)
@Composable
fun VendorProfileScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    profileViewModel: ProfileViewModel = koinInject(),
    authViewModel: AuthViewModel = koinInject(),
    dataStore: LocalDataStore = koinInject()
) {
    val vendorProfileState by profileViewModel.vendorProfileState.collectAsState()
    val logoutState by authViewModel.logoutState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showLogoutDialog by remember { mutableStateOf(false) }
    var backPressedTime by remember { mutableStateOf(0L) }

    // Double back press to exit
    BackHandler(enabled = true) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - backPressedTime < 2000) {
            exitApp()
        } else {
            backPressedTime = currentTime
            showToast("Press back again to exit")
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.getVendorProfile()
    }

    LaunchedEffect(logoutState) {
        when (logoutState) {
            is UiState.Success -> {
                Logger.withTag("LOGOUT_DEBUG").d { "VENDOR - Logout API Success, navigating to welcome" }
                authViewModel.resetAuthStates()
                profileViewModel.resetProfileStates()
                showToast("Logged out successfully")
                navController.navigate(AppScreenUser.LaunchWelcome) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is UiState.Error -> {
                Logger.withTag("LOGOUT_DEBUG").d { "VENDOR - Logout API Error, navigating to welcome" }
                authViewModel.resetAuthStates()
                profileViewModel.resetProfileStates()
                showToast("Logged out")
                navController.navigate(AppScreenUser.LaunchWelcome) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> Unit
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false
                coroutineScope.launch {
                    val logger = Logger.withTag("LOGOUT_DEBUG")
                    logger.d { "VENDOR - Logout button clicked" }

                    val tokenBefore = dataStore.getToken()
                    val refreshTokenBefore = dataStore.getRefreshToken()
                    val userRoleBefore = dataStore.getUserRole()
                    val userIdBefore = dataStore.getId()

                    logger.d { "VENDOR - Before clear - Token: $tokenBefore" }
                    logger.d { "VENDOR - Before clear - RefreshToken: $refreshTokenBefore" }
                    logger.d { "VENDOR - Before clear - UserRole: $userRoleBefore" }
                    logger.d { "VENDOR - Before clear - UserId: $userIdBefore" }

                    val refreshToken = dataStore.getRefreshToken() ?: ""

                    // Reset auth states FIRST to prevent auto-login
                    authViewModel.resetAuthStates()
                    profileViewModel.resetProfileStates()

                    // Clear everything IMMEDIATELY
                    dataStore.clearAll()
                    PlatformScheduler.cancelScheduledRefresh()
                    TokenProvider.token = null

                    val tokenAfter = dataStore.getToken()
                    val refreshTokenAfter = dataStore.getRefreshToken()
                    val userRoleAfter = dataStore.getUserRole()
                    val userIdAfter = dataStore.getId()

                    logger.d { "VENDOR - After clear - Token: $tokenAfter" }
                    logger.d { "VENDOR - After clear - RefreshToken: $refreshTokenAfter" }
                    logger.d { "VENDOR - After clear - UserRole: $userRoleAfter" }
                    logger.d { "VENDOR - After clear - UserId: $userIdAfter" }
                    logger.d { "VENDOR - TokenProvider.token: ${TokenProvider.token}" }
                    logger.d { "VENDOR - Datastore cleared, calling logout API" }

                    val logoutRequest = LogoutRequest(refreshToken = refreshToken)
                    authViewModel.logout(logoutRequest)
                }
            }
        )
    }

    when (vendorProfileState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CustomLoader()
            }
        }

        is UiState.Success -> {
            val profile = (vendorProfileState as UiState.Success).data

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(80.dp),
                                shape = MaterialTheme.shapes.large,
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Store,
                                        contentDescription = "Store",
                                        modifier = Modifier.size(40.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Text(
                                text = profile.storeName ?: "Store Name",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = profile.email ?: "No Email",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = profile.vendorName ?: "Vendor",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = profile.phone ?: "No Phone",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Store Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (!profile.vendorDescription.isNullOrBlank()) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column {
                                        Text(
                                            text = "Description",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = profile.vendorDescription,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                            if (!profile.address.isNullOrBlank()) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column {
                                        Text(
                                            text = "Address",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = profile.address,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                            if (!profile.collegeName.isNullOrBlank()) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column {
                                        Text(
                                            text = "College",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = profile.collegeName,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }

                            if (!profile.foodCategories.isNullOrEmpty()) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Restaurant,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Column {
                                        Text(
                                            text = "Food Categories",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = profile.foodCategories.filterNotNull().joinToString(", "),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Store Settings",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    VendorProfileOption(
                        icon = Icons.Default.Edit,
                        title = "Edit Profile",
                        onClick = {
                            navController.navigate(AppScreenVendor.VendorProfileUpdate)
                        }
                    )
                }

                item {
                    VendorProfileOption(
                        icon = Icons.Default.Lock,
                        title = "Change Password",
                        onClick = {
                            navController.navigate(AppScreenUser.ChangePassword)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Support",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    VendorProfileOption(
                        icon = Icons.AutoMirrored.Filled.Help,
                        title = "Help & Support",
                        onClick = {
                            navController.navigate(AppScreenVendor.HelpAndSupportScreenVendor)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout")
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = (vendorProfileState as UiState.Error).message ?: "Error loading profile",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Button(onClick = { profileViewModel.getVendorProfile() }) {
                        Text("Retry")
                    }
                }
            }
        }

        UiState.Empty -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CustomLoader()
            }
        }
    }
}
