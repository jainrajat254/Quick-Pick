package org.rajat.quickpick.presentation.feature.auth.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.auth.components.OnboardingScreenLayout
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.chocolateshake
import quickpick.shared.generated.resources.delivery

@Composable
fun OnboardingScreen3(
    navController: NavController,
    dataStore: LocalDataStore
) {
    val coroutineScope = rememberCoroutineScope()
    val logger = Logger.withTag("OnboardingScreen3")
    var showNotificationDialog by remember { mutableStateOf(false) }

    val navigateToLaunchWelcome: () -> Unit = {
        coroutineScope.launch {
            dataStore.setHasOnboarded(true)
            dataStore.setHasRequestedNotificationPermission(true)
            navController.navigate(AppScreenUser.LaunchWelcome) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    OnboardingScreen3Content(
        onGetStartedClick = {
            coroutineScope.launch {
                dataStore.setHasOnboarded(true)
            }
            showNotificationDialog = true
        },
        onSkipClick = {
            coroutineScope.launch {
                dataStore.setHasOnboarded(true)
            }
            showNotificationDialog = true
        }
    )

    if (showNotificationDialog) {
        NotificationPermissionDialogWrapper(
            onPermissionResult = { granted ->
                logger.d { "Permission result: $granted" }
                navigateToLaunchWelcome()
            },
            onDismiss = {
                showNotificationDialog = false
                navigateToLaunchWelcome()
            }
        )
    }
}

@Composable
private fun OnboardingScreen3Content(
    onGetStartedClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    OnboardingScreenLayout(
        progressStep = 3,
        imageContent = {
            Image(
                painter = painterResource(resource = Res.drawable.chocolateshake),
                contentDescription = "Chocolate Shake",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 53.dp)
                    .fillMaxSize(),
                alignment = Alignment.TopCenter
            )
        },
        cardIcon = painterResource(resource = Res.drawable.delivery),
        cardTitle = "Fast Delivery",
        cardDescription = "Hungry? We've got you covered! Get your order delivered right to your classroom, hostel, or library corner — super quick! Fresh, hot. On time.",
        buttonText = "Get Started",
        onButtonClick = onGetStartedClick,
        onSkipClick = onSkipClick,
        showSkip = false
    )
}

