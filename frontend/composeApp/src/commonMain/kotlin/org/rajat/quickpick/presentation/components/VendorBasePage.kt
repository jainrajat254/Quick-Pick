
package org.rajat.quickpick.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.presentation.navigation.BottomNavItem
import org.rajat.quickpick.presentation.navigation.VendorRoutes
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.bgrem
import quickpick.composeapp.generated.resources.bgremlight



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VendorBasePage(
    currentRoute: String = "vendor_dashboard",
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val rootScreens = listOf("vendor_dashboard", "vendor_orders", "vendor_menu", "vendor_profile")
    val showBackButton = currentRoute !in rootScreens && currentRoute.isNotEmpty()
    val vendorName: String = "My Store"
    val vendorEmail: String = "vendor@quickpick.com"

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
            route = VendorRoutes.VendorDashboard.route,
            label = "Dashboard",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            route = VendorRoutes.VendorOrders.route,
            label = "Orders",
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Filled.List,
            badgeCount = pendingOrderCount
        ),
        BottomNavItem(
            route = VendorRoutes.VendorMenu.route,
            label = "Menu",
            selectedIcon = Icons.Filled.Restaurant,
            unselectedIcon = Icons.Outlined.Restaurant
        ),
        BottomNavItem(
            route = VendorRoutes.VendorProfile.route,
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            VendorDrawerContent(
                vendorName = vendorName,
                vendorEmail = vendorEmail,
                onItemClick = { route ->
                    scope.launch {
                        drawerState.close()
                    }
                    onNavigate(route)
                }
            )
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            if(!showBackButton){
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
                            )
                        }
                    },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
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
}


@Composable
private fun VendorDrawerContent(
    vendorName: String,
    vendorEmail: String,
    onItemClick: (String) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ){
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = "Store Avatar",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text = vendorName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = vendorEmail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        val drawerItems = listOf(
            VendorDrawerMenuItem(
                route = VendorRoutes.VendorDashboard.route,
                label = "Dashboard",
                icon = Icons.Default.Dashboard
            ),
            VendorDrawerMenuItem(
                route = VendorRoutes.VendorOrders.route,
                label = "Orders",
                icon = Icons.Default.ShoppingBag
            ),
            VendorDrawerMenuItem(
                route = VendorRoutes.VendorMenu.route,
                label = "Menu Management",
                icon = Icons.Default.Restaurant
            ),
            VendorDrawerMenuItem(
                route = "vendor_settings",
                label = "Settings",
                icon = Icons.Default.Settings
            ),
            VendorDrawerMenuItem(
                route = "logout",
                label = "Logout",
                icon = Icons.AutoMirrored.Filled.ExitToApp
            )
        )

        drawerItems.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                selected = false,
                onClick = {
                    onItemClick(item.route)
                },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    unselectedContainerColor = MaterialTheme.colorScheme.surface,
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun VendorBottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount > 0) {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = MaterialTheme.colorScheme.onError
                                ) {
                                    Text(
                                        text = if (item.badgeCount > 99) "99+" else item.badgeCount.toString(),
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (currentRoute == item.route) {
                                item.selectedIcon
                            } else {
                                item.unselectedIcon
                            },
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    onItemClick(item.route)
                },
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
    return when (route) {
        "vendor_dashboard" -> "Dashboard"
        "vendor_orders" -> "Orders"
        "vendor_menu" -> "Menu"
        "vendor_profile" -> "Profile"
        "vendor_order_detail/{orderId}" -> "Order Details"
        else -> "QuickPick Vendor"
    }
}

data class VendorDrawerMenuItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)
