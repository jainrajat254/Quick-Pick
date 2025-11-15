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
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.payment
import quickpick.composeapp.generated.resources.pizza

@Composable
fun OnboardingScreen2(
    navController: NavController,
    dataStore: LocalDataStore
) {
    val coroutineScope = rememberCoroutineScope()

    OnboardingScreenLayout(
        progressStep = 2,
        imageContent = {
            Image(
                painter = painterResource(resource = Res.drawable.pizza),
                contentDescription = "Pizza",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 53.dp)
                    .fillMaxSize(),
                alignment = Alignment.TopCenter
            )
        },
        cardIcon = painterResource(resource = Res.drawable.payment),
        cardTitle = "Easy Payment",
        cardDescription = "No need to worry about cash! Pay instantly using UPI, cards, or your campus wallet. Fast, safe, and hassle-free checkout every time.",
        buttonText = "Next",
        onButtonClick = {
            navController.navigate(AppScreenUser.Onboarding3)
        },
        onSkipClick = {
            coroutineScope.launch {
                dataStore.setHasOnboarded(true)
                navController.navigate(AppScreenUser.LaunchWelcome) {
                    popUpTo(AppScreenUser.Onboarding2) { inclusive = true }
                }
            }
        },
        showSkip = true
    )
}