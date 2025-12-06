package org.rajat.quickpick.presentation.feature.vendor.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.rajat.quickpick.presentation.feature.vendor.dashboard.components.QuickActionsSection
import org.rajat.quickpick.presentation.feature.vendor.dashboard.components.StatsCard
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import org.rajat.quickpick.presentation.viewmodel.OrderViewModel
import org.rajat.quickpick.utils.BackHandler
import org.rajat.quickpick.utils.UiState
import org.rajat.quickpick.utils.exitApp
import org.rajat.quickpick.utils.toast.showToast

@OptIn(ExperimentalTime::class)
@Composable
fun VendorDashboardScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    orderViewModel: OrderViewModel
) {
    val vendorOrderStatsState by orderViewModel.vendorOrderStatsState.collectAsState()
    var backPressedTime by remember { mutableStateOf(0L) }

    // Double back press to exit
    BackHandler(enabled = true) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - backPressedTime < 2000) {
            exitApp()
        } else {
            backPressedTime = currentTime
            showToast("Press back again to exit")
        }
    }

    LaunchedEffect(Unit) {
        orderViewModel.getVendorOrderStats()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Overview",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            when (vendorOrderStatsState) {
                is UiState.Success -> {
                    val stats = (vendorOrderStatsState as UiState.Success).data
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatsCard(
                            title = "Total Orders",
                            value = stats.totalOrders.toString(),
                            icon = Icons.Default.ShoppingBag,
                            modifier = Modifier.weight(1f)
                        )
                        StatsCard(
                            title = "Pending",
                            value = stats.pendingOrders.toString(),
                            icon = Icons.Default.Pending,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatsCard(
                            title = "Total Orders",
                            value = "0",
                            icon = Icons.Default.ShoppingBag,
                            modifier = Modifier.weight(1f)
                        )
                        StatsCard(
                            title = "Pending",
                            value = "0",
                            icon = Icons.Default.Pending,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            when (vendorOrderStatsState) {
                is UiState.Success -> {
                    val stats = (vendorOrderStatsState as UiState.Success).data
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatsCard(
                            title = "Completed",
                            value = stats.completedOrders.toString(),
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.weight(1f)
                        )
                        StatsCard(
                            title = "Cancelled",
                            value = stats.cancelledOrders.toString(),
                            icon = Icons.Default.Cancel,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                else -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatsCard(
                            title = "Completed",
                            value = "0",
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.weight(1f)
                        )
                        StatsCard(
                            title = "Cancelled",
                            value = "0",
                            icon = Icons.Default.Cancel,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            QuickActionsSection(
                onViewOrders = {
                    navController.navigate(AppScreenVendor.VendorOrders)
                },
                onManageMenu = {
                    navController.navigate(AppScreenVendor.VendorMenu)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
