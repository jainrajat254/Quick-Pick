package org.rajat.quickpick.presentation.feature.auth.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Store
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
import org.rajat.quickpick.domain.modal.auth.RegisterVendorRequest
import org.rajat.quickpick.presentation.components.CustomDropdown
import org.rajat.quickpick.presentation.components.CustomLoader
import org.rajat.quickpick.presentation.components.CustomTextField
import org.rajat.quickpick.presentation.feature.auth.components.InlineClickableText
import org.rajat.quickpick.presentation.feature.auth.components.RegisterButton
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.viewmodel.AuthViewModel
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.Validators
import org.rajat.quickpick.utils.ErrorUtils
import org.rajat.quickpick.utils.toast.showToast
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.registerbackground
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun VendorRegisterScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    dataStore: LocalDataStore
) {
    val logger = Logger.withTag("VendorRegisterScreen")

    var vendorName by remember { mutableStateOf("") }
    var storeName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gstNumber by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var vendorDescription by remember { mutableStateOf("") }
    var foodCategories by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedCollege by remember { mutableStateOf("") }

    val isFormValid = Validators.isVendorFormValid(
        vendorName,
        storeName,
        email,
        phone,
        password,
        address,
        gstNumber,
        licenseNumber,
        selectedCollege
    )

    val vendorRegisterState by authViewModel.vendorRegisterState.collectAsState()

    LaunchedEffect(vendorRegisterState) {
        when (vendorRegisterState) {
            is UiState.Success -> {
                showToast("Registration successful. We've emailed a verification code.")
                val emailLower = email.trim().lowercase()
                dataStore.savePendingVerification(emailLower, "VENDOR")
                dataStore.setHasOnboarded(true)
                authViewModel.setPendingRegistrationCredentials(
                    email = emailLower,
                    password = password.trim(),
                    userType = "VENDOR"
                )
                authViewModel.resetAuthStates()
                navController.navigate(AppScreenUser.EmailOtpVerify(email = emailLower, userType = "VENDOR")) {
                    popUpTo(AppScreenUser.VendorRegister) { inclusive = true }
                    launchSingleTop = true
                }
            }

            is UiState.Error -> {
                val raw = (vendorRegisterState as UiState.Error).message
                val message = ErrorUtils.sanitizeError(raw)
                showToast(message)
                logger.e { raw ?: "Unknown error" }
                authViewModel.resetAuthStates()
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
                    .padding(16.dp)
                    .navigationBarsPadding(),
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
                                text = "Create Account",
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
                        value = vendorName,
                        onValueChange = { vendorName = it },
                        label = "Full Name",
                        leadingIcon = Icons.Filled.Person
                    )
                }

                item {
                    CustomTextField(
                        value = storeName,
                        onValueChange = { storeName = it },
                        label = "Store Name",
                        leadingIcon = Icons.Filled.Store
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
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        leadingIcon = Icons.Filled.Lock,
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        imeAction = ImeAction.Done
                    )
                }

                item {
                    CustomTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = "Address",
                        leadingIcon = Icons.Filled.Home
                    )
                }

                item {
                    CustomTextField(
                        value = gstNumber,
                        onValueChange = { gstNumber = it },
                        label = "GST Number",
                        leadingIcon = Icons.Filled.Badge
                    )
                }

                item {
                    CustomTextField(
                        value = licenseNumber,
                        onValueChange = { licenseNumber = it },
                        label = "License Number",
                        leadingIcon = Icons.Filled.Badge
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
                    CustomTextField(
                        value = vendorDescription,
                        onValueChange = { vendorDescription = it },
                        label = "Vendor Description",
                        leadingIcon = Icons.Filled.Info
                    )
                }

                item {
                    RegisterButton(
                        onClick = {
                            val registerVendorRequest = RegisterVendorRequest(
                                vendorName = vendorName.trim(),
                                storeName = storeName.trim(),
                                email = email.trim().lowercase(),
                                password = password.trim(),
                                phone = phone.trim(),
                                address = address.trim(),
                                gstNumber = gstNumber.trim(),
                                licenseNumber = licenseNumber.trim(),
                                collegeName = selectedCollege,
                                foodCategories = foodCategories,
                                vendorDescription = vendorDescription
                            )
                            authViewModel.registerVendor(registerVendorRequest)
                        },
                        enabled = isFormValid,
                        isLoading = vendorRegisterState is UiState.Loading,
                        text = "REGISTER",
                        loadingText = "Registering..."
                    )
                }
                item {
                    InlineClickableText(
                        normalText = "Already have an account?",
                        clickableText = "Sign in",
                        onClick = {
                            navController.navigate(AppScreenUser.VendorLogin) {
                                popUpTo(AppScreenUser.VendorRegister) { inclusive = true }
                            }
                        }
                    )
                }
            }
        }

        if (vendorRegisterState is UiState.Loading) {
            CustomLoader()
        }
    }
}
