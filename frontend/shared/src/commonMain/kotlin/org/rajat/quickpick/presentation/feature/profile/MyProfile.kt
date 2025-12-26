package org.rajat.quickpick.presentation.feature.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.compose.koinInject
import org.rajat.quickpick.domain.modal.profile.GetStudentProfileResponse
import org.rajat.quickpick.presentation.feature.profile.components.MyProfileFields
import org.rajat.quickpick.presentation.viewmodel.ProfileViewModel
import org.rajat.quickpick.utils.ErrorUtils
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.toast.showToast

@Composable
fun MyProfileScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    profileViewModel: ProfileViewModel = koinInject()
) {
    val studentProfileState by profileViewModel.studentProfileState.collectAsState()
    val updateProfileState by profileViewModel.updateStudentProfileState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isEditMode by remember { mutableStateOf(false) }
    var currentProfile by remember { mutableStateOf<GetStudentProfileResponse?>(null) }

    LaunchedEffect(Unit) {
        profileViewModel.getStudentProfile()
    }

    LaunchedEffect(studentProfileState) {
        when (studentProfileState) {
            is UiState.Success -> {
                val profile = (studentProfileState as UiState.Success<GetStudentProfileResponse>).data
                currentProfile = profile
                fullName = profile.fullName ?: ""
                phone = profile.phone ?: ""
            }
            is UiState.Error -> {
                val raw = (studentProfileState as UiState.Error).message
                val message = ErrorUtils.sanitizeError(raw)
                showToast(message)
            }
            else -> {}
        }
    }

    LaunchedEffect(updateProfileState) {
        when (updateProfileState) {
            is UiState.Success -> {
                showToast("Profile updated successfully")
                // Refresh the profile data
                profileViewModel.getStudentProfile()
                profileViewModel.resetProfileStates()
            }
            is UiState.Error -> {
                val raw = (updateProfileState as UiState.Error).message
                val message = ErrorUtils.sanitizeError(raw)
                showToast(message)
                profileViewModel.resetProfileStates()
            }
            else -> {}
        }
    }

    val isLoading = studentProfileState is UiState.Loading || updateProfileState is UiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                isLoading && currentProfile == null -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(top = 32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                currentProfile == null && studentProfileState is UiState.Error -> {
                    Text(
                        "Could not load profile.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { profileViewModel.getStudentProfile() }) {
                        Text("Retry")
                    }
                }
                currentProfile != null -> {
                    MyProfileFields(
                        fullName = fullName,
                        onFullNameChange = { fullName = it },
                        phone = phone,
                        onPhoneChange = { phone = it },
                        profile = currentProfile!!,
                        isLoading = isLoading,
                        isEditMode = isEditMode,
                        onEditModeChange = {
                            isEditMode = it
                            if (!it) {
                                // Reset to original values when canceling edit
                                fullName = currentProfile?.fullName ?: ""
                                phone = currentProfile?.phone ?: ""
                            }
                        },
                        onSaveProfile = { request ->
                            profileViewModel.updateStudentProfile(request)
                        }
                    )
                }
            }
        }
    }
}
