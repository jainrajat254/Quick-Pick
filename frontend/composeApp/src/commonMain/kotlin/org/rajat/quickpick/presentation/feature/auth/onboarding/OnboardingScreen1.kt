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
import quickpick.composeapp.generated.resources.burger
import quickpick.composeapp.generated.resources.orders
import quickpick.composeapp.generated.resources.pizza

@Composable
fun OnboardingScreen1(
    navController: NavController,
    dataStore: LocalDataStore
) {
    val coroutineScope = rememberCoroutineScope()

    OnboardingScreenLayout(
        progressStep = 1,
        imageContent = {
            Image(
                painter = painterResource(resource = Res.drawable.burger),
                contentDescription = "Burger",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        cardIcon = painterResource(resource = Res.drawable.orders),
        cardTitle = "Order For Food",
        cardDescription = "Craving something delicious?...",
        buttonText = "Next",
        onButtonClick = {
            coroutineScope.launch {
                dataStore.setHasOnboarded(true)
                navController.navigate(Routes.Onboarding2.route)
            }
        },
        onSkipClick = {
            coroutineScope.launch {
                dataStore.setHasOnboarded(true)
                navController.navigate(Routes.LaunchWelcome.route) {
                    popUpTo(Routes.Onboarding1.route) { inclusive = true }
                }
            }
        },
        showSkip = true
    )
}