package org.rajat.quickpick.presentation.feature.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.domain.modal.auth.ChangePasswordRequest
import org.rajat.quickpick.domain.modal.auth.ChangePasswordResponse
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.presentation.feature.profile.components.PasswordTextField
import org.rajat.quickpick.presentation.theme.AppColors
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Suppress("UNUSED_PARAMETER")
@Composable
fun ChangePasswordScreen(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    navController: NavHostController,
    authViewModel: AuthViewModel = koinInject(),
    dataStore: LocalDataStore = koinInject()
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var uiError by remember { mutableStateOf<String?>(null) }
    var uiSuccess by remember { mutableStateOf<String?>(null) }
    var userTypeForReset by remember { mutableStateOf("STUDENT") }
    val passwordsMatch = newPassword == confirmPassword
    val canChangePassword = currentPassword.isNotBlank() &&
            newPassword.isNotBlank() &&
            confirmPassword.isNotBlank() &&
            passwordsMatch

    val changePasswordState by authViewModel.changePasswordState.collectAsState()

    val effectiveLoading = isLoading || changePasswordState is UiState.Loading

    LaunchedEffect(changePasswordState) {
        when (changePasswordState) {
            is UiState.Success -> {
                val msg = (changePasswordState as UiState.Success<ChangePasswordResponse>).data.message ?: "Password changed successfully"
                uiSuccess = msg
                uiError = null
                showToast(msg)
                currentPassword = ""
                newPassword = ""
                confirmPassword = ""
                navController.navigateUp()
            }
            is UiState.Error -> {
                val err = (changePasswordState as UiState.Error).message ?: "Failed to change password"
                uiError = err
                uiSuccess = null
                showToast(err)
            }
            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        val role = dataStore.getUserRole()
        userTypeForReset = if (role.equals("VENDOR", true)) "VENDOR" else "STUDENT"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.surface)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { navController.navigateUp() }) {
                    Text("Back", style = MaterialTheme.typography.bodyMedium)
                }
            }

            PasswordTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = "Current Password",
                isVisible = currentPasswordVisible,
                onVisibilityChange = { currentPasswordVisible = !currentPasswordVisible },
                showLabel = false,
                placeholder = "Current Password"
            )

            TextButton(
                onClick = {
                    navController.navigate(org.rajat.quickpick.presentation.navigation.AppScreenUser.ForgotPassword(userType = userTypeForReset))
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Forgot Password?",
                    color = AppColors.Error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 16.dp)
                )
            }

            PasswordTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New Password",
                isVisible = newPasswordVisible,
                onVisibilityChange = { newPasswordVisible = !newPasswordVisible },
                showLabel = false,
                placeholder = "New Password"
            )
            Spacer(modifier = Modifier.height(16.dp))

            //Confirm New Password
            PasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm New Password",
                isVisible = confirmPasswordVisible,
                onVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible },
                showError = confirmPassword.isNotEmpty() && !passwordsMatch,
                showLabel = false,
                placeholder = "Confirm New Password"
            )
            if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                Text(
                    text = "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 4.dp, top = 2.dp)
                )
            }
            if (!uiError.isNullOrBlank()) {
                Text(
                    text = uiError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    uiError = null
                    uiSuccess = null
                    val token = TokenProvider.token
                    if (token.isNullOrBlank()) {
                        uiError = "Session expired. Please login again."
                        showToast(uiError!!)
                        return@Button
                    }
                    authViewModel.changePassword(
                        ChangePasswordRequest(
                            currentPassword = currentPassword.trim(),
                            newPassword = newPassword.trim()
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !effectiveLoading && canChangePassword,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.Warning,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (effectiveLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Change Password", fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
