package org.rajat.quickpick.presentation.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.rajat.quickpick.presentation.feature.profile.components.SettingComponentCard
import org.rajat.quickpick.presentation.navigation.Routes


@Composable
fun SettingsScreen(
    paddingValues: PaddingValues,
    navController: NavController
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SettingComponentCard(
            text = "Notifications",
            icon = Icons.Filled.Notifications,
            onClick = {
                navController.navigate(Routes.NotificationSetting.route)
            }
        )
        SettingComponentCard(
            text = "Change Password",
            icon = Icons.Filled.Key,
            onClick = {
                navController.navigate(Routes.ChangePassword.route)
            }
        )
    }
}
