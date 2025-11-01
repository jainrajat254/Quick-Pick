package org.rajat.quickpick.presentation.feature.auth.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.domain.modal.auth.LoginUserRequest
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.CustomTextField
import org.rajat.quickpick.presentation.feature.auth.components.InlineClickableText
import org.rajat.quickpick.presentation.feature.auth.components.RegisterButton
import org.rajat.quickpick.presentation.navigation.Routes
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.Validators.isLoginFormValid
import org.rajat.quickpick.utils.toast.showToast
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.burger
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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

    LaunchedEffect(userLoginState) {
        when (userLoginState) {
            is UiState.Success -> {
                val response = (userLoginState as UiState.Success<LoginUserResponse>).data
                TokenProvider.token = response.tokens.accessToken
                dataStore.saveToken(response.tokens.accessToken)
                dataStore.saveRefreshToken(response.tokens.refreshToken)

                val expiryMillis = Clock.System.now().toEpochMilliseconds() + (response.tokens.expiresIn * 1000)
                dataStore.saveTokenExpiryMillis(expiryMillis)

                dataStore.saveId(response.userId)
                dataStore.saveUserProfile(response)
                showToast("User Signed In Successfully")
                navController.navigate(Routes.Home.route) {
                    popUpTo(Routes.UserLogin.route) { inclusive = true }
                }
            }

            is UiState.Error -> {
                val message = (userLoginState as UiState.Error).message ?: "Unknown error"
                showToast(message)
                logger.e { message }
            }

            else -> Unit
        }
    }

    Scaffold {
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
                )
        ) {

            Image(
                painter = painterResource(resource = Res.drawable.burger),
                contentDescription = "Background Image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
            Card(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 480.dp)
                    .shadow(
                        elevation = 32.dp,
                        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
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
                            navController.navigate(Routes.UserRegister.route) {
                                popUpTo(Routes.UserLogin.route) { inclusive = true }
                            }
                        }
                    )
                }
            }
            if (userLoginState is UiState.Loading) {
                CustomLoader()
            }
        }
    }
}
