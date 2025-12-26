package org.rajat.quickpick.presentation.feature.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor

@Composable
fun NotificationPermissionScreen(
    navController: NavController,
    dataStore: LocalDataStore = koinInject(),
    onPermissionGranted: () -> Unit = {},
    suppressAutoNavigate: Boolean = false
) {
    val logger = Logger.withTag("NotificationPermissionScreen")

    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }

    val navigateToNextScreen: () -> Unit = {
        scope.launch {
            logger.d { "Marking notification permission as requested" }
            dataStore.setHasRequestedNotificationPermission(true)

            val token = dataStore.getToken()
            val userRole = dataStore.getUserRole()

            logger.d { "Token: $token, UserRole: $userRole" }

            if (!token.isNullOrEmpty()) {
                when (userRole) {
                    "VENDOR" -> {
                        logger.d { "Navigating to VendorDashboard" }
                        navController.navigate(AppScreenVendor.VendorDashboard) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    "USER" -> {
                        logger.d { "Navigating to HomeScreen" }
                        navController.navigate(AppScreenUser.HomeScreen) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                    else -> {
                        logger.d { "Invalid role, navigating to LaunchWelcome" }
                        navController.navigate(AppScreenUser.LaunchWelcome) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            } else {
                // No token, navigate to welcome screen
                logger.d { "No token found, navigating to LaunchWelcome" }
                navController.navigate(AppScreenUser.LaunchWelcome) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = RoundedCornerShape(60.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))


                Text(
                    text = "Stay Updated!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Get real-time updates about your orders, special offers, and important notifications.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    NotificationFeatureItem(
                        icon = "📦",
                        text = "Order status updates"
                    )
                    NotificationFeatureItem(
                        icon = "🎉",
                        text = "Exclusive deals and offers"
                    )
                    NotificationFeatureItem(
                        icon = "⚡",
                        text = "Important announcements"
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        isLoading = true
                        onPermissionGranted()
                        if (!suppressAutoNavigate) {
                            navigateToNextScreen()
                        } else {
                            logger.d { "Auto navigation suppressed by wrapper; waiting for wrapper to handle navigation." }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Allow Notifications",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                TextButton(
                    onClick = {
                        logger.d { "User skipped notification permission" }
                        navigateToNextScreen()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading
                ) {
                    Text(
                        text = "Maybe Later",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NotificationFeatureItem(
    icon: String,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = icon,
            fontSize = 24.sp,
            modifier = Modifier.width(40.dp)
        )
        Text(
            text = text,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}
