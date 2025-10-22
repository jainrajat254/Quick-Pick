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
fun OnboardingScreen1C(
    onGetStartedClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    OnboardingScreenLayout(
        progressStep = 3,
        imageContent = {
            Image(
                painter = painterResource(id = R.drawable.chocolateshake),
                contentDescription = "Ice Cream and Brownie",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        cardIcon = painterResource(id = R.drawable.delivery),
        cardTitle = "Fast Delivery",
        cardDescription = "Hungry? We've got you covered! Get your order delivered right to your classroom, hostel, or library corner — super quick! Fresh, hot. On time.",
        buttonText = "Get Started",
        onButtonClick = onGetStartedClick,
        onSkipClick = onSkipClick,
        showSkip = false
    )
}
