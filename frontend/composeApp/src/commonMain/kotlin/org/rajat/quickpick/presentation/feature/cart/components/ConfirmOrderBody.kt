package org.rajat.quickpick.presentation.feature.cart.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmOrderBody(
    orderId : String?
){
    Icon(
        imageVector = Icons.Default.CheckCircleOutline,
        contentDescription = "Success",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(100.dp)
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        "Order Placed Successfully!",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )

    if (orderId != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.ReceiptLong,
                contentDescription = "Order ID",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Order ID: $orderId",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.surfaceTint
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        "Your order is confirmed. You can track its status in the 'My Orders' section. Please proceed to the vendor to pick up your order and pay in cash.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 32.dp)
    )
}