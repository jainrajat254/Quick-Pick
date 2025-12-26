package org.rajat.quickpick

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.rajat.quickpick.data.local.LocalDataStore

@Composable
expect fun NotificationPermissionScreenWrapper(
    navController: NavHostController,
    dataStore: LocalDataStore
)

