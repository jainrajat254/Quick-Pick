package org.rajat.quickpick.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.presentation.navigation.BottomNavItem
import org.rajat.quickpick.presentation.navigation.AppScreenVendor
import quickpick.shared.generated.resources.Res
import quickpick.shared.generated.resources.bgrem
import quickpick.shared.generated.resources.bgremlight
import kotlin.text.contains


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorBasePage(
    currentRoute: String = AppScreenVendor.VendorDashboard::class.simpleName!!,
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val rootScreens = listOf(
        AppScreenVendor.VendorDashboard::class.simpleName,
        AppScreenVendor.VendorOrders::class.simpleName,
        AppScreenVendor.VendorMenu::class.simpleName,
        AppScreenVendor.VendorProfile::class.simpleName
    )
    val showBackButton = !rootScreens.any { currentRoute.contains(it!!) } && currentRoute.isNotEmpty()

    val orderViewModel: org.rajat.quickpick.presentation.viewmodel.OrderViewModel = org.koin.compose.koinInject()
    val pendingOrdersState by orderViewModel.pendingOrdersState.collectAsState()

    val pendingOrderCount = remember(pendingOrdersState) {
        if (pendingOrdersState is org.rajat.quickpick.utils.UiState.Success) {
            (pendingOrdersState as org.rajat.quickpick.utils.UiState.Success).data.orders?.size ?: 0
        } else {
            0
        }
    }

    LaunchedEffect(Unit) {
        orderViewModel.getPendingOrdersForVendor()
    }

    val bottomNavItems = listOf(
        BottomNavItem(
            route = AppScreenVendor.VendorDashboard::class.simpleName!!,
            label = "Dashboard",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            route = AppScreenVendor.VendorOrders::class.simpleName!!,
            label = "Orders",
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Outlined.List,
            badgeCount = pendingOrderCount
        ),
        BottomNavItem(
            route = AppScreenVendor.VendorMenu::class.simpleName!!,
            label = "Menu",
            selectedIcon = Icons.Filled.Restaurant,
            unselectedIcon = Icons.Outlined.Restaurant
        ),
        BottomNavItem(
            route = AppScreenVendor.VendorProfile::class.simpleName!!,
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        val isProfileRoot = currentRoute.contains(AppScreenVendor.VendorProfile::class.simpleName!!)
                        if(!showBackButton && !isProfileRoot){
                            Icon(
                                painter = if (isSystemInDarkTheme()) {
                                    painterResource(resource = Res.drawable.bgremlight)
                                } else {
                                    painterResource(resource = Res.drawable.bgrem)
                                },
                                contentDescription = null,
                                modifier = Modifier.size(35.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                        }
                        Text(
                            text = getVendorScreenTitle(currentRoute),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 28.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            VendorBottomNavigationBar(
                items = bottomNavItems,
                currentRoute = currentRoute,
                onItemClick = { route ->
                    onNavigate(route)
                }
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
private fun VendorBottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute.contains(item.route)
            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ) {
                                    Text(text = if (item.badgeCount > 99) "99+" else item.badgeCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(text = item.label, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

private fun getVendorScreenTitle(route: String): String {
    return when {
        route.contains(AppScreenVendor.VendorOrderDetail::class.simpleName!!) -> "Order Details"
        route.contains(AppScreenVendor.VendorDashboard::class.simpleName!!) -> "Dashboard"
        route.contains(AppScreenVendor.VendorOrders::class.simpleName!!) -> "Orders"
        route.contains(AppScreenVendor.VendorMenu::class.simpleName!!) -> "Menu"
        route.contains(AppScreenVendor.VendorProfile::class.simpleName!!) -> "Profile"
        route.contains("VendorSettings") -> "Settings"
        route.contains(AppScreenVendor.AddMenuItemScreen::class.simpleName!!) -> "Add New Menu Item"
        route.contains(AppScreenVendor.UpdateMenuItemScreen::class.simpleName!!) -> "Edit Menu Item"
        else -> "QuickPick Vendor"
    }
}
