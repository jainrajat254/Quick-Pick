package org.rajat.quickpick.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.data.dummy.DummyData
import org.rajat.quickpick.presentation.theme.AppTheme

import org.jetbrains.compose.resources.painterResource
import quickpick.composeapp.generated.resources.Res
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.rajat.quickpick.presentation.components.RegisterComponents
import quickpick.composeapp.generated.resources.registerbackground


@Composable
fun UserRegisterScreen(
    onRegisterClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
) {

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedCollege by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var selectedBranch by remember { mutableStateOf("") }

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
                    ){
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
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
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        leadingIcon = Icons.Filled.Person,
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
                        value = studentId,
                        onValueChange = { studentId = it },
                        label = "Student ID",
                        leadingIcon = Icons.Filled.Badge,
                        keyboardType = KeyboardType.Text
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

                    RegisterComponents.CustomDropdown(
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
                        RegisterComponents.CustomDropdown(
                            value = selectedGender,
                            onValueChange = { selectedGender = it },
                            label = "Gender",
                            leadingIcon = Icons.Filled.Person,
                            options = DummyData.genders,
                            modifier = Modifier.weight(1f),

                        )

                        RegisterComponents.CustomDropdown(
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
                    RegisterComponents.RegisterButton(
                        onClick = {
                            onRegisterClick()
                        },
                        enabled = fullName.isNotBlank() &&
                                email.isNotBlank() &&
                                phone.isNotBlank() &&
                                studentId.isNotBlank() &&
                                password.isNotBlank() &&
                                selectedCollege.isNotBlank() &&
                                selectedGender.isNotBlank() &&
                                selectedBranch.isNotBlank()
                    )

                }
                item{
                    Spacer(modifier = Modifier.height(8.dp))
                }


            }
        }
    }
}
