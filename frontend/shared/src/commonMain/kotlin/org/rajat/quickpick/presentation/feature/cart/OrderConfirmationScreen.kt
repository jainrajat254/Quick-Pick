package org.rajat.quickpick.presentation.feature.cart


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.cart.components.ActionButtons
import org.rajat.quickpick.presentation.feature.cart.components.ConfirmOrderBody
import org.rajat.quickpick.presentation.theme.AppTheme

@Composable
fun OrderConfirmationScreen(
    paddingValues: PaddingValues,
    orderId: String?,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        ConfirmOrderBody(orderId)

        Spacer(modifier = Modifier.height(32.dp))

        //Action Buttons
        ActionButtons(
            orderId = orderId,
            navController = navController
        )
    }
}
