package org.rajat.quickpick.presentation.components

import androidx.compose.foundation.background
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
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.rajat.quickpick.presentation.navigation.BottomNavItem
import org.rajat.quickpick.presentation.navigation.Routes
import quickpick.composeapp.generated.resources.Res
import quickpick.composeapp.generated.resources.bgrem
import quickpick.composeapp.generated.resources.bgremlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasePage(

    currentRoute: String = "home",
    onBackClick: () -> Unit,
    onNavigate: (String) -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val rootScreens = listOf("home", "cart", "orders", "profile")
    val showBackButton = currentRoute !in rootScreens && currentRoute.isNotEmpty()
    //change it later with fetched data
    val userName: String = "Current User"
    val userEmail: String = "currentuser@gmail.com"

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomNavItems = listOf(
        BottomNavItem(
            route = "home",
            label = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            route = Routes.Cart.route,
            label = "Cart",
            selectedIcon = Icons.Filled.ShoppingCart,
            unselectedIcon = Icons.Outlined.ShoppingCart
        ),
        BottomNavItem(
            route = Routes.Orders.route,
            label = "Orders",
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Filled.List
        ),
        BottomNavItem(
            route = Routes.Profile.route,
            label = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                userName = userName,
                userEmail = userEmail,
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
                                text = getScreenTitle(currentRoute),
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

@Composable
private fun BottomNavigationBar(
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
                    Icon(
                        imageVector = if (currentRoute == item.route) {
                            item.selectedIcon
                        } else {
                            item.unselectedIcon
                        },
                        contentDescription = item.label
                    )
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
    return when (route) {
        "home" -> "QuickPick"
        "cart" -> "My Cart"
        "orders" -> "My Orders"
        "profile" -> "Profile"
        "order_details/{orderId}" -> "Order Details"
        "my_orders" -> "My Orders"
        "review_order/{order_Id}" -> "Review Order"
        "cancel_order/{orderId}" -> "Cancel Order"
        "my_profile" -> "My Profile"
        "contact_us" -> "Get In Touch"
        "change_password"->"Change Password"
        "notification_setting"->"Notification Setting"
        "settings"->"Settings"
        "cart"-> "My Cart"
        "checkout"-> "Confirm Order"
        "help_and_faqs"->"Help & FAQs"


        else -> "QuickPick"
    }
}
//data object ReviewOrder : Routes("review_order/{orderId}") {
//    fun createRoute(orderId: String) = "review_order/$orderId"
//}
//data object CancelOrderConfirmation : Routes("cancel_order_confirmation")
//data object ReviewOrderConfirmation : Routes("review_order_confirmation")
//data object MyProfile : Routes("my_profile")
//data object ContactUs : Routes("contact_us")
////    data object AboutUs : Routes("about_us")
//data object ChangePassword : Routes("change_password")
//data object NotificationSetting : Routes("notification_setting")
//data object Settings : Routes("settings")





data class DrawerMenuItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)