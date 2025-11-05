package org.rajat.quickpick.presentation.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.presentation.feature.profile.components.MyProfileFields

@Composable
fun MyProfileScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    profile: GetStudentProfileResponse?,
    isLoading: Boolean,
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }

    // it updates the local state when the profile from the viewmodel changes
    LaunchedEffect(profile) {
        fullName = profile?.fullName ?: ""
        phone = profile?.phone ?: ""
        department = profile?.department ?: ""
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (profile == null) {
                Text(
                    "Could not load profile.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 32.dp)
                )
            } else {
                MyProfileFields(
                    fullName = fullName,
                    onFullNameChange = { fullName = it },
                    phone = phone,
                    onPhoneChange = { phone = it },
                    department = department,
                    onDepartmentChange = { department = it },
                    profile = profile,
                    isLoading = isLoading
                )
            }
        }
    }
}

