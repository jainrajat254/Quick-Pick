package org.rajat.quickpick.presentation.feature.auth.password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import org.rajat.quickpick.domain.modal.auth.ForgotPasswordRequest
import org.rajat.quickpick.domain.modal.auth.PasswordOtpRequest
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.CustomTextField
import org.rajat.quickpick.presentation.feature.auth.components.RegisterButton
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    userType: String
) {
    var email by remember { mutableStateOf("") }
    var cooldownActive by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(60) }

    val forgotState by authViewModel.forgotPasswordState.collectAsState()
    val sendPasswordOtpState by authViewModel.sendPasswordOtpState.collectAsState()

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

    LaunchedEffect(forgotState) {
        when (forgotState) {
            is UiState.Success -> {
                showToast("If the account exists, a password reset code has been sent")
                cooldownActive = true
            }
            is UiState.Error -> {
                val msg = (forgotState as UiState.Error).message
                if (!msg.isNullOrBlank()) showToast(msg)
            }
            else -> Unit
        }
    }

    LaunchedEffect(sendPasswordOtpState) {
        when (sendPasswordOtpState) {
            is UiState.Success -> {
                showToast("If the account exists, a password reset code has been sent")
                cooldownActive = true
            }
            is UiState.Error -> {
                val msg = (sendPasswordOtpState as UiState.Error).message
                if (!msg.isNullOrBlank()) showToast(msg)
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
                    text = "Forgot Password",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Enter your email to receive a 6-digit reset code",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email",
                    leadingIcon = Icons.Filled.Email,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )

                RegisterButton(
                    onClick = {
                        val e = email.trim().lowercase()
                        if (e.isNotBlank()) {
                            authViewModel.forgotPassword(
                                ForgotPasswordRequest(email = e, userType = userType)
                            )
                        } else showToast("Enter email")
                    },
                    text = "Send Code",
                    loadingText = "Sending...",
                    isLoading = forgotState is UiState.Loading,
                    enabled = email.isNotBlank() && !cooldownActive && forgotState !is UiState.Loading
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (cooldownActive) "Resend in ${secondsLeft}s" else "Need a new code?",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Button(
                        enabled = !cooldownActive && sendPasswordOtpState !is UiState.Loading && email.isNotBlank(),
                        onClick = {
                            authViewModel.sendPasswordOtp(
                                PasswordOtpRequest(email = email.trim().lowercase(), userType = userType)
                            )
                        }
                    ) { Text("Resend") }
                }

                Button(
                    onClick = {
                        val e = email.trim().lowercase()
                        if (e.isNotBlank()) {
                            navController.navigate(AppScreenUser.ResetPasswordOtp(email = e, userType = userType))
                        }
                    },
                    enabled = email.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("I have a code") }
            }
        }

        if (forgotState is UiState.Loading || sendPasswordOtpState is UiState.Loading) {
            CustomLoader()
        }
    }
}
