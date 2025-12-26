package org.rajat.quickpick.presentation.feature.vendor.dashboard.components

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rajat.quickpick.presentation.theme.AppColors
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.managemenuimg
import quickpick.shared.generated.resources.vieworderimage
import quickpick.shared.generated.resources.vieworderimg

@Composable
fun QuickActionsSection(
    onViewOrders: () -> Unit,
    onManageMenu: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        DashboardActionCard(
            title = "View Orders",
            subtitle = "Track and manage incoming orders",
            imageRes = Res.drawable.vieworderimage,
            backgroundColor = AppColors.SecondaryContainer,
            onClick = onViewOrders
        )

        DashboardActionCard(
            title = "Manage Menu",
            subtitle = "Update items, prices & availability",
            imageRes = Res.drawable.managemenuimg,
            backgroundColor = AppColors.PrimaryContainer,
            onClick = onManageMenu
        )
    }

}
