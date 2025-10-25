package org.rajat.quickpick.presentation.feature.myorders.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.domain.modal.ordermanagement.OrderStatus
import org.rajat.quickpick.presentation.theme.AppColors

@Composable
fun StatusChip(status: OrderStatus) {
    val backgroundColor = when (status) {
        OrderStatus.COMPLETED -> AppColors.SuccessContainer
        OrderStatus.CANCELLED, OrderStatus.REJECTED -> MaterialTheme.colorScheme.errorContainer
        OrderStatus.PENDING -> AppColors.WarningContainer
        OrderStatus.ACCEPTED,
        OrderStatus.PREPARING,
        OrderStatus.READY_FOR_PICKUP -> MaterialTheme.colorScheme.secondaryContainer
    }

    val contentColor = when (status) {
        OrderStatus.COMPLETED -> AppColors.Success
        OrderStatus.CANCELLED, OrderStatus.REJECTED -> MaterialTheme.colorScheme.error
        OrderStatus.PENDING -> AppColors.Warning
        OrderStatus.ACCEPTED,
        OrderStatus.PREPARING,
        OrderStatus.READY_FOR_PICKUP -> MaterialTheme.colorScheme.secondary
    }

    val icon = when (status) {
        OrderStatus.COMPLETED -> Icons.Default.CheckCircle
        OrderStatus.CANCELLED, OrderStatus.REJECTED -> Icons.Default.Warning // Use Error icon
        OrderStatus.READY_FOR_PICKUP -> Icons.Default.ThumbUp
        else -> Icons.Default.Info
    }

    val text = when (status) {
        OrderStatus.COMPLETED -> "Order delivered"
        OrderStatus.CANCELLED, OrderStatus.REJECTED -> "Order Cancelled"
        OrderStatus.PENDING -> "Pending Confirmation"
        OrderStatus.ACCEPTED -> "Order Accepted"
        OrderStatus.PREPARING -> "Food is Preparing"
        OrderStatus.READY_FOR_PICKUP -> "Ready for Pickup"
    }
    Surface(
        shape = RoundedCornerShape(50),
        color = backgroundColor,
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Status",
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}