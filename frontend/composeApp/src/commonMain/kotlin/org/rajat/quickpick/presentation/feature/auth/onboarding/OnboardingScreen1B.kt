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
fun OnboardingScreen1B(
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {}
) {
    OnboardingScreenLayout(
        progressStep = 2,
        imageContent = {
            Image(
                painter = painterResource(id = R.drawable.pizza),
                contentDescription = "Ice Cream and Brownie",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize(1f)
                    .clip(RoundedCornerShape(8.dp))
            )
        },
        cardIcon = painterResource(id = R.drawable.payment),
        cardTitle = "Easy Payment",
        cardDescription = "No need to worry about cash! Pay instantly using UPI, cards, or your campus wallet. Fast, safe, and hassle-free checkout every time.",
        buttonText = "Next",
        onButtonClick = onNextClick,
        onSkipClick = onSkipClick,
        showSkip = true
    )
}