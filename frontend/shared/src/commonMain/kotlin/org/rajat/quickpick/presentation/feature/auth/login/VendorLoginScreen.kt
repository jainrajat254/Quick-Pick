package org.rajat.quickpick.presentation.feature.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.domain.modal.auth.LoginVendorRequest
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.CustomTextField
import org.rajat.quickpick.presentation.feature.auth.components.InlineClickableText
import org.rajat.quickpick.presentation.feature.auth.components.RegisterButton
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.presentation.viewmodel.ProfileViewModel
import org.koin.compose.koinInject
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.Validators.isLoginFormValid
import org.rajat.quickpick.utils.toast.showToast
import org.rajat.quickpick.utils.session.AuthSessionSaver
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.burger
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun VendorLoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    dataStore: LocalDataStore,
    profileViewModel: ProfileViewModel = koinInject()
) {

    val logoutLogger = Logger.withTag("LOGOUT_DEBUG")

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isFormValid = isLoginFormValid(email = email, password = password)

    val vendorLoginState by authViewModel.vendorLoginState.collectAsState()

    LaunchedEffect(Unit) {
        logoutLogger.d { "VENDOR_LOGIN - Screen loaded" }
        logoutLogger.d { "VENDOR_LOGIN - Current vendorLoginState: $vendorLoginState" }
    }

    DisposableEffect(Unit) {
        onDispose {
            val logoutLogger = Logger.withTag("LOGOUT_DEBUG")
            logoutLogger.d { "VENDOR_LOGIN - Screen disposed, resetting auth states" }
            authViewModel.resetAuthStates()
        }
    }

    LaunchedEffect(vendorLoginState) {
        val fcmLogger = Logger.withTag("FCMDEBUG")
        fcmLogger.d { "========================================" }
        fcmLogger.d { "VENDOR_LOGIN - LaunchedEffect triggered" }
        fcmLogger.d { "VENDOR_LOGIN - State: $vendorLoginState" }
        logoutLogger.d { "VENDOR_LOGIN - LaunchedEffect triggered, vendorLoginState: $vendorLoginState" }
        when (vendorLoginState) {
            is UiState.Success -> {
                fcmLogger.d { "VENDOR_LOGIN - SUCCESS STATE DETECTED" }
                logoutLogger.d { "VENDOR_LOGIN - Success state detected, auto-logging in!" }
                val response = (vendorLoginState as UiState.Success<LoginVendorResponse>).data
                fcmLogger.d { "VENDOR_LOGIN - Response received, userId: ${response.userId}" }
                fcmLogger.d { "VENDOR_LOGIN - Calling AuthSessionSaver.saveVendorSession" }
                logoutLogger.d { "VENDOR_LOGIN - Calling AuthSessionSaver.saveVendorSession" }
                AuthSessionSaver.saveVendorSession(dataStore, response)
                fcmLogger.d { "VENDOR_LOGIN - AuthSessionSaver.saveVendorSession completed" }
                fcmLogger.d { "========================================" }
                showToast("Vendor Signed In Successfully")
                // Fetch vendor profile to get verification status before navigating
                logoutLogger.d { "VENDOR_LOGIN - Fetching vendor profile to check verification status" }
                profileViewModel.getVendorProfile()
            }

            else -> {
                logoutLogger.d { "VENDOR_LOGIN - State is Empty or Loading" }
            }
        }
    }

    val vendorProfileState by profileViewModel.vendorProfileState.collectAsState()

    LaunchedEffect(vendorProfileState) {
        when (vendorProfileState) {
            is UiState.Success -> {
                val profile = (vendorProfileState as UiState.Success).data
                val status = profile.verificationStatus ?: "PENDING"
                if (status == "VERIFIED") {
                    logoutLogger.d { "VENDOR_LOGIN - Verified: navigating to dashboard" }
                    profileViewModel.resetProfileStates()
                    authViewModel.resetAuthStates()
                    navController.navigate(AppScreenVendor.VendorDashboard) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (status == "PENDING") {
                    logoutLogger.d { "VENDOR_LOGIN - Pending verification: navigating to pending screen" }
                    profileViewModel.resetProfileStates()
                    authViewModel.resetAuthStates()
                    navController.navigate(AppScreenVendor.VendorVerificationPending) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                } else if (status == "REJECTED") {
                    logoutLogger.d { "VENDOR_LOGIN - Rejected: navigating to pending screen (rejected)" }
                    profileViewModel.resetProfileStates()
                    authViewModel.resetAuthStates()
                    navController.navigate(AppScreenVendor.VendorVerificationPending) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            is UiState.Error -> {
                logoutLogger.d { "VENDOR_LOGIN - Error fetching profile: navigating to dashboard" }
                profileViewModel.resetProfileStates()
                authViewModel.resetAuthStates()
                navController.navigate(AppScreenVendor.VendorDashboard) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
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
                                    text = "Vendor Sign In",
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
                                navController.navigate(AppScreenUser.ForgotPassword(userType = "VENDOR"))
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
                                    val loginVendorRequest =
                                        LoginVendorRequest(
                                            email = email.trim().lowercase(),
                                            password = password.trim()
                                        )
                                    authViewModel.loginVendor(request = loginVendorRequest)
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
                                navController.navigate(AppScreenUser.VendorRegister) {
                                    popUpTo(AppScreenUser.VendorLogin) { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }

            if (vendorLoginState is UiState.Loading) {
                CustomLoader()
            }
        }
    }
}