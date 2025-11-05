package org.rajat.quickpick.presentation.feature.cart.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.presentation.navigation.Routes

@Composable
fun ActionButtons(
    orderId:String?,
    navController : NavController
){

    Column(
        modifier = Modifier.fillMaxWidth(0.8f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { if (orderId != null) navController.navigate(Routes.OrderDetail.route)},
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = orderId != null,
            shape = RoundedCornerShape(50)
        ) {
            Text("View Order Details", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = {
                navController.navigate(Routes.Home.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text("Back to Home")
        }
    }
}
