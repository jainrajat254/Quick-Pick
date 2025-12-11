package org.rajat.quickpick.presentation.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
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
import org.rajat.quickpick.presentation.navigation.AppScreenUser
import org.rajat.quickpick.presentation.navigation.BottomNavItem
import org.rajat.quickpick.utils.BackHandler
import org.rajat.quickpick.utils.exitApp
import org.rajat.quickpick.utils.toast.showToast
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun BasePage(

    currentRoute: String = AppScreenUser.HomeScreen::class.simpleName.toString(),
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val rootScreens = listOf(
        AppScreenUser.HomeScreen::class.simpleName,
        AppScreenUser.Cart::class.simpleName,
        AppScreenUser.Orders::class.simpleName,
        AppScreenUser.Profile::class.simpleName
    )
    val isRootScreen = currentRoute in rootScreens
    val showBackButton = !isRootScreen && currentRoute.isNotEmpty()

    var backPressedTime by remember { mutableStateOf(0L) }

    BackHandler(enabled = isRootScreen) {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime - backPressedTime < 2000) {
            exitApp()
        } else {
            backPressedTime = currentTime
            showToast("Press back again to exit")
        }
    }

    val cartViewModel: org.rajat.quickpick.presentation.viewmodel.CartViewModel = org.koin.compose.koinInject()
    val cartState by cartViewModel.cartState.collectAsState()

    val cartItemCount = remember(cartState) {
        if (cartState is org.rajat.quickpick.utils.UiState.Success) {
            (cartState as org.rajat.quickpick.utils.UiState.Success).data.itemCount
        } else {
            0
        }
    }

    LaunchedEffect(Unit) {
        cartViewModel.getCart()
    }

    val bottomNavItems = listOf(
        BottomNavItem(
            route = AppScreenUser.HomeScreen::class.simpleName!!,
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            route = AppScreenUser.Cart::class.simpleName!!,
            label = "Cart",
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart,
            badgeCount = cartItemCount
        ),
        BottomNavItem(
            route = AppScreenUser.Orders::class.simpleName!!,
            label = "Orders",
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Filled.List
        ),
        BottomNavItem(
            route = AppScreenUser.Profile::class.simpleName!!,
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Text(
                            text = getScreenTitle(currentRoute),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 28.sp,
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
            BottomNavigationBar(
                items = bottomNavItems,
                currentRoute = currentRoute,
                onItemClick = { route ->
                    onNavigate(route as String)
                }
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
private fun DrawerContent(
    userName: String,
    userEmail: String,
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
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Avatar",
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
                        text = userName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = userEmail,
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
            DrawerMenuItem(
                route = "my_profile",
                label = "My Profile",
                icon = Icons.Default.Person
            ),
            DrawerMenuItem(
                route = "my_orders",
                label = "My Orders",
                icon = Icons.Default.ShoppingBag
            ),
            DrawerMenuItem(
                route = "favorites",
                label = "Favorites",
                icon = Icons.Default.Favorite
            ),
            DrawerMenuItem(
                route = "privacy_settings",
                label = "Privacy Settings",
                icon = Icons.Default.Lock
            ),
            DrawerMenuItem(
                route = "notifications",
                label = "Notifications",
                icon = Icons.Default.Notifications
            ),
            DrawerMenuItem(
                route = "contact_us",
                label = "Contact Us",
                icon = Icons.Default.Email
            ),
            DrawerMenuItem(
                route = "about",
                label = "About",
                icon = Icons.Default.Info
            ),
            DrawerMenuItem(
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

// This is your original BottomNavigationBar implementation
@Composable
private fun BottomNavigationBar(
    items: List<BottomNavItem>,
    currentRoute: Any,
    onItemClick: (Any) -> Unit
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


private fun getScreenTitle(route: String): String {
    return when {
        route.contains(AppScreenUser.OrderDetail::class.simpleName!!) -> "Order Details"
        route.contains(AppScreenUser.ReviewOrder::class.simpleName!!) -> "Review Order"
        route.contains(AppScreenUser.CancelOrder::class.simpleName!!) -> "Cancel Order"
        route.contains(AppScreenUser.VendorDetail::class.simpleName!!) -> "Vendor Details"
        route.contains(AppScreenUser.MyProfile::class.simpleName!!) -> "My Profile"
        route.contains(AppScreenUser.ContactUs::class.simpleName!!) -> "Get In Touch"
        route.contains(AppScreenUser.ChangePassword::class.simpleName!!) -> "Change Password"
        route.contains(AppScreenUser.NotificationSetting::class.simpleName!!) -> "Notification Setting"
        route.contains(AppScreenUser.Settings::class.simpleName!!) -> "Settings"
        route.contains(AppScreenUser.Checkout::class.simpleName!!) -> "Confirm Order"
        route.contains(AppScreenUser.HelpAndFaqs::class.simpleName!!) -> "Help & FAQs"
        route.contains(AppScreenUser.HomeScreen::class.simpleName!!) -> "QuickPick"
        route.contains(AppScreenUser.Cart::class.simpleName!!) -> "My Cart"
        route.contains(AppScreenUser.Orders::class.simpleName!!) -> "My Orders"
        route.contains(AppScreenUser.Profile::class.simpleName!!) -> "Profile"
        route.contains("my_profile") -> "My Profile"
        route.contains("my_orders") -> "My Orders"
        route.contains("contact_us") -> "Get In Touch"
        else -> "QuickPick"
    }
}
data class DrawerMenuItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)
