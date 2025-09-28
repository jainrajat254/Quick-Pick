package org.rajat.quickpick.presentation.navigation

import org.jetbrains.compose.resources.DrawableResource

data class BottomNavItem(
    val title: String,
    val icon: DrawableResource,
    val route: String,
)