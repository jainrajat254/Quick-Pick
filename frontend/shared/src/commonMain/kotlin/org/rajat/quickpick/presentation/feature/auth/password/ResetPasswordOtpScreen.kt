package org.rajat.quickpick.presentation.feature.auth.password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.domain.modal.auth.PasswordOtpRequest
import org.rajat.quickpick.domain.modal.auth.ResetPasswordOtpRequest
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.CustomTextField
import org.rajat.quickpick.presentation.feature.auth.components.RegisterButton
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun ResetPasswordOtpScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    email: String,
    userType: String
) {
    var otp by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var cooldownActive by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(60) }

    val sendPasswordOtpState by authViewModel.sendPasswordOtpState.collectAsState()
    val resetPasswordOtpState by authViewModel.resetPasswordOtpState.collectAsState()

    val passwordsMatch = newPassword.isNotBlank() && newPassword == confirmPassword

    LaunchedEffect(cooldownActive) {
        if (cooldownActive) {
            secondsLeft = 60
            while (secondsLeft > 0 && cooldownActive) {
                kotlinx.coroutines.delay(1000)
                secondsLeft--
            }
            if (cooldownActive) cooldownActive = false
        }
    }

    LaunchedEffect(sendPasswordOtpState) {
        when (sendPasswordOtpState) {
            is UiState.Success -> {
                showToast("If the account exists, a password reset code has been sent")
                cooldownActive = true
                errorMessage = null
            }
            is UiState.Error -> {
                val msg = (sendPasswordOtpState as UiState.Error).message
                errorMessage = msg
                if (!msg.isNullOrBlank()) showToast(msg)
            }
            else -> Unit
        }
    }

    LaunchedEffect(resetPasswordOtpState) {
        when (resetPasswordOtpState) {
            is UiState.Success -> {
                showToast("Password reset successful")
                if (userType.equals("VENDOR", true)) {
                    navController.navigate(AppScreenUser.VendorLogin) { popUpTo(0) { inclusive = true } }
                } else {
                    navController.navigate(AppScreenUser.UserLogin) { popUpTo(0) { inclusive = true } }
                }
            }
            is UiState.Error -> {
                val msg = (resetPasswordOtpState as UiState.Error).message
                errorMessage = msg
                showToast(msg ?: "Failed")
                if (msg != null && (msg.contains("expired", true) || msg.contains("Too many attempts", true))) {
                    cooldownActive = false
                    secondsLeft = 0
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
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reset Password",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Enter the 6-digit code sent to $email",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                CustomTextField(
                    value = otp,
                    onValueChange = { input -> otp = input.filter { it.isDigit() }.take(6) },
                    label = "OTP Code",
                    leadingIcon = Icons.Filled.Lock,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    maxLength = 6
                )

                CustomTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = "New Password",
                    leadingIcon = Icons.Filled.Lock,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )

                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = "Confirm Password",
                    leadingIcon = Icons.Filled.Lock,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    maxLength = 50
                )

                if (!errorMessage.isNullOrBlank()) {
                    Text(errorMessage!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                if (confirmPassword.isNotBlank() && !passwordsMatch) {
                    Text("Passwords do not match", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }

                RegisterButton(
                    onClick = {
                        if (otp.length == 6 && passwordsMatch) {
                            authViewModel.resetPasswordOtp(
                                ResetPasswordOtpRequest(
                                    email = email,
                                    userType = userType,
                                    otp = otp,
                                    newPassword = newPassword
                                )
                            )
                        } else {
                            showToast("Fill all fields correctly")
                        }
                    },
                    text = "Reset Password",
                    loadingText = "Resetting...",
                    isLoading = resetPasswordOtpState is UiState.Loading,
                    enabled = otp.length == 6 && passwordsMatch
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (cooldownActive) "Resend in ${secondsLeft}s" else "Didn't get code?",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(
                        enabled = !cooldownActive && sendPasswordOtpState !is UiState.Loading,
                        onClick = {
                            authViewModel.sendPasswordOtp(
                                PasswordOtpRequest(email = email, userType = userType)
                            )
                        }
                    ) { Text("Resend Code") }
                }
            }
        }

        if (sendPasswordOtpState is UiState.Loading || resetPasswordOtpState is UiState.Loading) {
            CustomLoader()
        }
    }
}
