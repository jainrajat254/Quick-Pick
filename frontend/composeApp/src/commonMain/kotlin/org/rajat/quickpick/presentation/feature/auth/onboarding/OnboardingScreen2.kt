package org.rajat.quickpick.presentation.feature.auth.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        cardIcon = painterResource(resource = Res.drawable.payment),
        cardTitle = "Easy Payment",
        cardDescription = "No need to worry about cash! Pay instantly using UPI, cards, or your campus wallet. Fast, safe, and hassle-free checkout every time.",
        buttonText = "Next",
        onButtonClick = {
            navController.navigate(Routes.Onboarding3.route)
        },
        onSkipClick = {
            coroutineScope.launch {
                dataStore.setHasOnboarded(true)
                navController.navigate(Routes.LaunchWelcome.route) {
                    popUpTo(Routes.Onboarding2.route) { inclusive = true }
                }
            }
        },
        showSkip = true
    )
}