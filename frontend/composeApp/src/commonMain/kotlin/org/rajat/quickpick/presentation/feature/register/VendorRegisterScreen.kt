package org.rajat.quickpick.presentation.feature.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
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
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.data.dummy.DummyData
import org.rajat.quickpick.presentation.components.RegisterComponents
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.registerbackground


@Composable
fun VendorRegisterScreen(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {

    var vendorName by remember { mutableStateOf("") }
    var storeName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gstNumber by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
    var foodLicenseNumber by remember { mutableStateOf("") }
    var vendorDescription by remember { mutableStateOf("") }
    var foodCategories by remember { mutableStateOf<List<String>>(emptyList()) } //multiple selectable chips
    var selectedCollege by remember { mutableStateOf("") }

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
            painter = painterResource(
                resource = Res.drawable.registerbackground
            ),
            contentDescription = "Background Image",
            contentScale = ContentScale.FillBounds,

            )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 180.dp)
                .shadow(
                    elevation = 32.dp,
                    shape = RoundedCornerShape(
                        topStart = 40.dp,
                        topEnd = 40.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    ),

                    ),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(
                topStart = 40.dp,
                topEnd = 40.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
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
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Create Account",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }

                item {
                    RegisterComponents.CustomTextField(
                        value = vendorName,
                        onValueChange = { vendorName = it },
                        label = "Full Name",
                        leadingIcon = Icons.Filled.Person,
                        keyboardType = KeyboardType.Text
                    )
                }

                item {
                    RegisterComponents.CustomTextField(
                        value = storeName,
                        onValueChange = { storeName = it },
                        label = "Store Name",
                        leadingIcon = Icons.Filled.School,
                        keyboardType = KeyboardType.Text
                    )
                }

                item {
                    RegisterComponents.CustomTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        leadingIcon = Icons.Filled.Email,
                        keyboardType = KeyboardType.Email
                    )
                }

                item {
                    RegisterComponents.CustomTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number",
                        leadingIcon = Icons.Filled.Phone,
                        keyboardType = KeyboardType.Phone
                    )
                }

                item {
                    RegisterComponents.CustomTextField(
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
                    RegisterComponents.CustomTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = "Address",
                        leadingIcon = Icons.Filled.Badge,
                        keyboardType = KeyboardType.Text
                    )
                }

                item {
                    RegisterComponents.CustomTextField(
                        value = gstNumber,
                        onValueChange = { gstNumber = it },
                        label = "GST Number",
                        leadingIcon = Icons.Filled.Badge,
                        keyboardType = KeyboardType.Text
                    )
                }

                item {
                    RegisterComponents.CustomTextField(
                        value = licenseNumber,
                        onValueChange = { licenseNumber = it },
                        label = "License Number",
                        leadingIcon = Icons.Filled.Badge,
                        keyboardType = KeyboardType.Text
                    )
                }

                item {
                    RegisterComponents.CustomTextField(
                        value = foodLicenseNumber,
                        onValueChange = { foodLicenseNumber = it },
                        label = "Food License Number",
                        leadingIcon = Icons.Filled.Badge,
                        keyboardType = KeyboardType.Text
                    )
                }

                item {
                    RegisterComponents.CustomDropdown(
                        value = selectedCollege,
                        onValueChange = { selectedCollege = it },
                        label = "Select College",
                        leadingIcon = Icons.Filled.School,
                        options = DummyData.colleges.map { it.name }
                    )

                }

                item {
                    RegisterComponents.CustomTextField(
                        value = vendorDescription,
                        onValueChange = { vendorDescription = it },
                        label = "Vendor Description",
                        leadingIcon = Icons.Filled.Badge,
                        keyboardType = KeyboardType.Text
                    )
                }





                item {
                    RegisterComponents.RegisterButton(
                        onClick = {
                            onRegisterClick()
                        },
                        enabled = vendorName.isNotBlank()
                                && storeName.isNotBlank()
                                && email.isNotBlank() &&
                                phone.isNotBlank() &&
                                password.isNotBlank()
                                && address.isNotBlank()
                                && gstNumber.isNotBlank()
                                && licenseNumber.isNotBlank()
                                && foodLicenseNumber.isNotBlank()
                                && selectedCollege.isNotBlank()
                    )

                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }


            }
        }
    }
}

