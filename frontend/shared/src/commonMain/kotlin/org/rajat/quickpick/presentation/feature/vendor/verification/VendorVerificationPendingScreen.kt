package org.rajat.quickpick.presentation.feature.vendor.verification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import org.rajat.quickpick.domain.modal.profile.VendorVerificationStatusResponse
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.koin.compose.koinInject
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.presentation.viewmodel.ProfileViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.ErrorUtils

private val verificationLogger = Logger.withTag("VendorVerificationPending")

@Composable
fun VendorVerificationPendingScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = koinInject(),
    authViewModel: AuthViewModel = koinInject()
) {
    val verificationStatusState by profileViewModel.vendorVerificationStatusState.collectAsState()

    // Clear all states when this screen is mounted to avoid stale state issues
    DisposableEffect(Unit) {
        verificationLogger.d { "VendorVerificationPendingScreen mounted - clearing all profile states" }
        profileViewModel.resetProfileStates()
        onDispose {
            verificationLogger.d { "VendorVerificationPendingScreen disposed" }
        }
    }

    LaunchedEffect(verificationStatusState) {
        verificationLogger.d { "verificationStatusState changed: $verificationStatusState" }
        when (verificationStatusState) {
            is UiState.Success<*> -> {
                val response = (verificationStatusState as UiState.Success<VendorVerificationStatusResponse>).data
                val status = response.status
                verificationLogger.d { "Received verification status: $status for email: ${response.email}" }
                when (status.uppercase()) {
                    "VERIFIED" -> {
                        verificationLogger.d { "Status is VERIFIED - navigating to dashboard" }
                        profileViewModel.resetProfileStates()
                        showToast("Your account has been verified!")
                        navController.navigate(AppScreenVendor.VendorDashboard) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    "REJECTED" -> {
                        verificationLogger.d { "Status is REJECTED - showing toast" }
                        showToast("Your account verification was rejected. Please contact support.")
                        profileViewModel.resetProfileStates()
                    }
                    "PENDING" -> {
                        verificationLogger.d { "Status is PENDING - showing toast" }
                        showToast("Your account is still pending approval")
                        profileViewModel.resetProfileStates()
                    }
                    else -> {
                        verificationLogger.w { "Unknown verification status: $status" }
                        showToast("Unknown verification status: $status")
                        profileViewModel.resetProfileStates()
                    }
                }
            }
            is UiState.Error -> {
                val raw = (verificationStatusState as UiState.Error).message
                verificationLogger.e { "Error fetching verification status: $raw" }
                showToast(ErrorUtils.sanitizeError(raw))
                profileViewModel.resetProfileStates()
            }
            else -> {
                verificationLogger.d { "verificationStatusState is Empty or Loading" }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.HourglassEmpty,
                            contentDescription = "Pending",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Account Pending Approval",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Your vendor account is awaiting admin approval. We'll notify you when it's approved.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        verificationLogger.d { "Refresh Status button clicked" }
                        profileViewModel.checkVendorVerificationStatus()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 6.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 0.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Refresh Status",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = {
                        verificationLogger.d { "Back to Login button clicked - clearing auth and navigating" }
                        authViewModel.logout()
                        profileViewModel.resetProfileStates()
                        navController.navigate(AppScreenUser.VendorLogin) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 0.dp)
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Back to Login",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

//                // View Profile Button
//                FilledTonalButton(
//                    onClick = {
//                        navController.navigate(AppScreenVendor.VendorProfile) {
//                            launchSingleTop = true
//                        }
//                    },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(56.dp),
//                    colors = ButtonDefaults.filledTonalButtonColors(
//                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
//                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
//                    )
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Person,
//                        contentDescription = "Profile",
//                        modifier = Modifier
//                            .size(20.dp)
//                            .padding(end = 0.dp)
//                    )
//                    Spacer(modifier = Modifier.size(8.dp))
//                    Text(
//                        text = "View Profile",
//                        style = MaterialTheme.typography.labelLarge,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
            }
        }
    }
}
