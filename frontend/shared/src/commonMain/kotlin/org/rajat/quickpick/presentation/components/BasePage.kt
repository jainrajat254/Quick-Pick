package org.rajat.quickpick.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        contentWindowInsets = WindowInsets.safeDrawing,
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
            BottomNavigationBar(
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
private fun BottomNavigationBar(
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
                                    Text(text = item.badgeCount.toString())
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
