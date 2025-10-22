package org.rajat.quickpick.presentation.feature.auth.onboarding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dummyproject.R

@Composable
fun OnboardingScreen1A(
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    OnboardingScreenLayout(
        progressStep = 1,
        imageContent = {
            Image(
                painter = painterResource(id = R.drawable.burger),
                contentDescription = "Ice Cream and Brownie",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        cardIcon = painterResource(id = R.drawable.orders),
        cardTitle = "Order For Food",
        cardDescription = "Craving something delicious? Browse your campus canteen, hostel kitchens, and local cafes — all in one app. Tap, choose, and order, then you’re in seconds!",
        buttonText = "Next",
        onButtonClick = onNextClick,
        onSkipClick = onSkipClick,
        showSkip = true
    )
}
