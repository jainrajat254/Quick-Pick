package org.rajat.quickpick.presentation.feature.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.domain.modal.auth.LoginUserRequest
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.CustomTextField
import org.rajat.quickpick.presentation.feature.auth.components.InlineClickableText
import org.rajat.quickpick.presentation.feature.auth.components.RegisterButton
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.Validators.isLoginFormValid
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.session.AuthSessionSaver
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.burger
import kotlin.time.ExperimentalTime
import org.rajat.quickpick.utils.ErrorUtils

@OptIn(ExperimentalTime::class)
@Preview
@Composable
fun UserLoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    dataStore: LocalDataStore
) {

    val logger = Logger.withTag("UserLoginScreen")

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isFormValid = isLoginFormValid(email = email, password = password)

    val userLoginState by authViewModel.userLoginState.collectAsState()

    LaunchedEffect(Unit) {
        val logoutLogger = Logger.withTag("LOGOUT_DEBUG")
        logoutLogger.d { "USER_LOGIN - Screen loaded" }
        logoutLogger.d { "USER_LOGIN - Current userLoginState: $userLoginState" }
    }

    DisposableEffect(Unit) {
        onDispose {
            val logoutLogger = Logger.withTag("LOGOUT_DEBUG")
            logoutLogger.d { "USER_LOGIN - Screen disposed, resetting auth states" }
            authViewModel.resetAuthStates()
        }
    }

    LaunchedEffect(userLoginState) {
        val logoutLogger = Logger.withTag("LOGOUT_DEBUG")
        val fcmLogger = Logger.withTag("FCMDEBUG")
        fcmLogger.d { "========================================" }
        fcmLogger.d { "USER_LOGIN - LaunchedEffect triggered" }
        fcmLogger.d { "USER_LOGIN - State: $userLoginState" }
        logoutLogger.d { "USER_LOGIN - LaunchedEffect triggered, userLoginState: $userLoginState" }
        when (userLoginState) {
            is UiState.Success -> {
                fcmLogger.d { "USER_LOGIN - SUCCESS STATE DETECTED" }
                val response = (userLoginState as UiState.Success<LoginUserResponse>).data
                fcmLogger.d { "USER_LOGIN - Response received, userId: ${response.userId}" }
                fcmLogger.d { "USER_LOGIN - Calling AuthSessionSaver.saveUserSession" }
                AuthSessionSaver.saveUserSession(dataStore, response)
                fcmLogger.d { "USER_LOGIN - AuthSessionSaver.saveUserSession completed" }
                fcmLogger.d { "========================================" }
                showToast("User Signed In Successfully")
                navController.navigate(AppScreenUser.HomeScreen) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }

            is UiState.Error -> {
                val raw = (userLoginState as UiState.Error).message
                fcmLogger.d { "USER_LOGIN - ERROR STATE: $raw" }
                val message = raw ?: "Unknown error"
                if (message.contains("verify your email", ignoreCase = true)) {
                    val emailLower = email.trim().lowercase()
                    navController.navigate(AppScreenUser.EmailOtpVerify(email = emailLower, userType = "STUDENT")) {
                        popUpTo(AppScreenUser.UserLogin) { inclusive = true }
                        launchSingleTop = true
                    }
                } else {
                    logger.e { "Login error: $raw" }
                    showToast(ErrorUtils.sanitizeError(raw))
                }
            }

            else -> Unit
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.burger),
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val screenHeight = maxHeight
                val cardTopPadding = (screenHeight * 0.5f).coerceAtLeast(300.dp)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(top = cardTopPadding),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 24.dp)
                            .imePadding(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier
                                .width(240.dp)
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "User Sign In",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        CustomTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            leadingIcon = Icons.Filled.Email,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )

                        CustomTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            leadingIcon = Icons.Filled.Lock,
                            isPassword = true,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )

                        TextButton(
                            onClick = {
                                navController.navigate(AppScreenUser.ForgotPassword(userType = "STUDENT"))
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(vertical = 0.dp)
                        ) {
                            Text("Forgot Password?", color = MaterialTheme.colorScheme.primary)
                        }

                        RegisterButton(
                            onClick = {
                                if (isFormValid) {
                                    val loginUserRequest =
                                        LoginUserRequest(
                                            email = email.trim().lowercase(),
                                            password = password.trim()
                                        )
                                    authViewModel.loginUser(request = loginUserRequest)
                                } else {
                                    showToast("Invalid email or password")
                                }
                            },
                            text = "Sign In",
                            loadingText = "Signing In...",
                            isLoading = false,
                            enabled = isFormValid
                        )

                        InlineClickableText(
                            normalText = "Don't have an account?",
                            clickableText = "Sign up",
                            onClick = {
                                navController.navigate(AppScreenUser.UserRegister) {
                                    popUpTo(AppScreenUser.UserLogin) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }

            if (userLoginState is UiState.Loading) {
                CustomLoader()
            }
        }
    }
}
