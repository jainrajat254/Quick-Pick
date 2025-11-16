package org.rajat.quickpick.presentation.feature.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import org.rajat.quickpick.data.dummy.DummyData
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.RegisterUserRequest
import org.rajat.quickpick.presentation.components.CustomDropdown
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.CustomTextField
import org.rajat.quickpick.presentation.feature.auth.components.InlineClickableText
import org.rajat.quickpick.presentation.feature.auth.components.RegisterButton
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.Validators
import org.rajat.quickpick.utils.toast.showToast
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.registerbackground
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun UserRegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    dataStore: LocalDataStore
) {
    val logger = Logger.withTag("UserRegisterScreen")

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedCollege by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedBranch by remember { mutableStateOf("") }

    val isFormValid = Validators.isUserFormValid(
        fullName = fullName,
        email = email,
        phone = phone,
        studentId = studentId,
        password = password,
        collegeName = selectedCollege,
        gender = selectedGender,
        branch = selectedBranch
    )


    val userRegisterState by authViewModel.userRegisterState.collectAsState()

    LaunchedEffect(userRegisterState) {
        when (userRegisterState) {
            is UiState.Success -> {
                val response = (userRegisterState as UiState.Success<LoginUserResponse>).data
                Logger.withTag("REGISTER").d { "USER_REGISTER success: userId=${response.userId}" }
                showToast("Registration successful. We've emailed a verification code.")
                val emailLower = email.trim().lowercase()
                dataStore.savePendingVerification(emailLower, "STUDENT")
                dataStore.setHasOnboarded(true)
                authViewModel.setPendingRegistrationCredentials(
                    email = emailLower,
                    password = password.trim(),
                    userType = "STUDENT"
                )
                navController.navigate(AppScreenUser.EmailOtpVerify(email = emailLower, userType = "STUDENT")) {
                    popUpTo(AppScreenUser.UserRegister) { inclusive = true }
                    launchSingleTop = true
                }
            }
            is UiState.Error -> {
                val message = (userRegisterState as UiState.Error).message ?: "Unknown error"
                Logger.withTag("REGISTER").d { "USER_REGISTER error: $message" }
                showToast(message)
                logger.e { message }
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
            )
    ) {
        Image(
            painter = painterResource(resource = Res.drawable.registerbackground),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 180.dp)
                .shadow(
                    elevation = 32.dp,
                    shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
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
                                text = "User Register",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                item {
                    CustomTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        leadingIcon = Icons.Filled.Person
                    )
                }

                item {
                    CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        leadingIcon = Icons.Filled.Email,
                        keyboardType = KeyboardType.Email
                    )
                }

                item {
                    CustomTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number",
                        leadingIcon = Icons.Filled.Phone,
                        keyboardType = KeyboardType.Phone,
                        maxLength = 10
                    )
                }

                item {
                    CustomTextField(
                        value = studentId,
                        onValueChange = { studentId = it },
                        label = "Student ID",
                        leadingIcon = Icons.Filled.Badge
                    )
                }

                item {
                    CustomTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        leadingIcon = Icons.Filled.Lock,
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                }

                item {
                    CustomDropdown(
                        value = selectedCollege,
                        onValueChange = { selectedCollege = it },
                        label = "Select College",
                        leadingIcon = Icons.Filled.School,
                        options = DummyData.colleges.map { it.name }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomDropdown(
                            value = selectedGender,
                            onValueChange = { selectedGender = it },
                            label = "Gender",
                            leadingIcon = Icons.Filled.Person,
                            options = DummyData.genders,
                            modifier = Modifier.weight(1f)
                        )
                        CustomDropdown(
                            value = selectedBranch,
                            onValueChange = { selectedBranch = it },
                            label = "Branch",
                            leadingIcon = Icons.Filled.School,
                            options = DummyData.branches.map { it.name },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    RegisterButton(
                        onClick = {
                            if (isFormValid) {
                                val registerUserRequest = RegisterUserRequest(
                                    fullName = fullName.trim(),
                                    email = email.trim().lowercase(),
                                    password = password.trim(),
                                    phone = phone.trim(),
                                    studentId = studentId.trim(),
                                    collegeName = selectedCollege,
                                    gender = selectedGender,
                                    department = selectedBranch
                                )
                                authViewModel.registerUser(registerUserRequest)
                            } else {
                                showToast("Please fill all fields correctly")
                            }
                        },
                        enabled = isFormValid,
                        isLoading = userRegisterState is UiState.Loading,
                        text = "REGISTER",
                        loadingText = "Registering..."
                    )

                }
                item {
                    InlineClickableText(
                        normalText = "Already have an account?",
                        clickableText = "Sign in",
                        onClick = {
                            navController.navigate(AppScreenUser.UserLogin) {
                                popUpTo(AppScreenUser.UserRegister) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
        if (userRegisterState is UiState.Loading) {
            CustomLoader()
        }
    }
}