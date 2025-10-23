package org.rajat.quickpick.presentation.feature.auth.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.presentation.feature.auth.components.OnboardingScreenLayout
import org.rajat.quickpick.presentation.navigation.Routes
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.chocolateshake
import quickpick.composeapp.generated.resources.delivery

@Composable
fun OnboardingScreen3(
    navController: NavController,
    dataStore: LocalDataStore
) {
    val coroutineScope = rememberCoroutineScope()

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
        onButtonClick = {
            coroutineScope.launch {
                dataStore.setHasOnboarded(true)
                navController.navigate(Routes.LaunchWelcome.route) {
                    popUpTo(Routes.Onboarding3.route) { inclusive = true }
                }
            }
        },
        onSkipClick = {
            coroutineScope.launch {
                dataStore.setHasOnboarded(true)
                navController.navigate(Routes.LaunchWelcome.route) {
                    popUpTo(Routes.Onboarding3.route) { inclusive = true }
                }
            }
        },
        showSkip = false
    )
}

