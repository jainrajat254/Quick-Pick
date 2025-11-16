package org.rajat.quickpick.presentation.feature.auth.verify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.domain.modal.auth.EmailOtpRequest
import org.rajat.quickpick.domain.modal.auth.EmailOtpVerifyRequest
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.CustomTextField
import org.rajat.quickpick.presentation.feature.auth.components.RegisterButton
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import org.rajat.quickpick.utils.session.AuthSessionSaver

@Composable
fun EmailOtpVerifyScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    email: String,
    userType: String,
    dataStore: LocalDataStore
) {
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var secondsLeft by remember { mutableIntStateOf(60) }
    var cooldownActive by remember { mutableStateOf(true) }

    val sendState by authViewModel.sendEmailOtpState.collectAsState()
    val verifyState by authViewModel.verifyEmailOtpState.collectAsState()
    val userLoginState by authViewModel.userLoginState.collectAsState()
    val vendorLoginState by authViewModel.vendorLoginState.collectAsState()

    LaunchedEffect(Unit) {
        cooldownActive = true
        secondsLeft = 60
        while (secondsLeft > 0 && cooldownActive) {
            kotlinx.coroutines.delay(1000)
            secondsLeft -= 1
        }
        cooldownActive = false
    }

    LaunchedEffect(sendState) {
        when (sendState) {
            is UiState.Success -> {
                showToast("Verification code sent")
                cooldownActive = true
                secondsLeft = 60
                errorMessage = null
            }
            is UiState.Error -> {
                val msg = (sendState as UiState.Error).message
                errorMessage = msg
                showToast(msg ?: "Failed to send code")
            }
            else -> Unit
        }
    }

    LaunchedEffect(verifyState) {
        when (verifyState) {
            is UiState.Success -> {
                dataStore.clearPendingVerification()

                val pending = authViewModel.consumePendingRegistrationCredentials()
                if (pending != null) {
                    val (pendingEmail, pendingPassword, pendingType) = pending
                    if (pendingType.equals("VENDOR", true)) {
                        authViewModel.loginVendor(org.rajat.quickpick.domain.modal.auth.LoginVendorRequest(pendingEmail, pendingPassword))
                    } else {
                        authViewModel.loginUser(org.rajat.quickpick.domain.modal.auth.LoginUserRequest(pendingEmail, pendingPassword))
                    }
                } else {
                    showToast("Email verified successfully. Please sign in.")
                    if (userType.equals("VENDOR", true)) {
                        navController.navigate(AppScreenUser.VendorLogin) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(AppScreenUser.UserLogin) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            }
            is UiState.Error -> {
                val msg = (verifyState as UiState.Error).message
                errorMessage = msg
                showToast(msg ?: "Verification failed")
            }
            else -> Unit
        }
    }

    LaunchedEffect(userLoginState) {
        when (userLoginState) {
            is UiState.Success -> {
                val response = (userLoginState as UiState.Success<LoginUserResponse>).data
                AuthSessionSaver.saveUserSession(dataStore, response)
                showToast("Welcome!")
                navController.navigate(AppScreenUser.HomeScreen) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is UiState.Error -> {
                val msg = (userLoginState as UiState.Error).message
                if (!msg.isNullOrBlank()) showToast(msg)
                navController.navigate(AppScreenUser.UserLogin) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> Unit
        }
    }

    LaunchedEffect(vendorLoginState) {
        when (vendorLoginState) {
            is UiState.Success -> {
                val response = (vendorLoginState as UiState.Success<LoginVendorResponse>).data
                AuthSessionSaver.saveVendorSession(dataStore, response)
                showToast("Welcome!")
                navController.navigate(org.rajat.quickpick.presentation.navigation.AppScreenVendor.VendorDashboard) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is UiState.Error -> {
                val msg = (vendorLoginState as UiState.Error).message
                if (!msg.isNullOrBlank()) showToast(msg)
                navController.navigate(AppScreenUser.VendorLogin) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> Unit
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Verify your email",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "We sent a 6-digit code to $email",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                CustomTextField(
                    value = otp,
                    onValueChange = { input ->
                        val filtered = input.filter { it.isDigit() }.take(6)
                        otp = filtered
                    },
                    label = "Enter 6-digit code",
                    leadingIcon = Icons.Filled.Lock,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                    maxLength = 6
                )

                if (!errorMessage.isNullOrBlank()) {
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                }

                RegisterButton(
                    onClick = {
                        if (otp.length == 6) {
                            authViewModel.verifyEmailOtp(
                                EmailOtpVerifyRequest(
                                    email = email,
                                    userType = userType,
                                    otp = otp
                                )
                            )
                        } else {
                            showToast("Please enter the 6-digit code")
                        }
                    },
                    text = "Verify",
                    loadingText = "Verifying...",
                    isLoading = verifyState is UiState.Loading,
                    enabled = otp.length == 6
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (cooldownActive) "Resend in ${secondsLeft}s" else "Didn't receive the code?",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(
                        enabled = !cooldownActive && sendState !is UiState.Loading,
                        onClick = {
                            authViewModel.sendEmailOtp(
                                EmailOtpRequest(email = email, userType = userType)
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!cooldownActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text("Resend Code")
                    }
                }
            }
        }

        if (sendState is UiState.Loading || verifyState is UiState.Loading) {
            CustomLoader()
        }
    }
}
