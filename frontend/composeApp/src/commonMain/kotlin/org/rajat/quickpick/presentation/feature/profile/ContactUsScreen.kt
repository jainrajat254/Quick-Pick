package org.rajat.quickpick.presentation.feature.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rajat.quickpick.presentation.feature.profile.components.ContactInfoItem

@Composable
fun ContactUsScreen(
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    val contactEmail = "jainraja170@gmail.com"
    val contactPhone = "+91 90275 61613"
    val contactAddress = "Kiet Group Of Institutions,Ghaziabad"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            "Have questions or need support? Reach out to us!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        //Contact Methods
        ContactInfoItem(
            icon = Icons.Default.Email,
            label = "Email Us",
            value = contactEmail,
            onClick = { /* TODO: Open email client */ }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ContactInfoItem(
            icon = Icons.Default.Phone,
            label = "Call Us",
            value = contactPhone,
            onClick = { /* TODO: Open dialer */ }
        )
        Spacer(modifier = Modifier.height(16.dp))
        ContactInfoItem(
            icon = Icons.Default.LocationOn,
            label = "Our Address",
            value = contactAddress,
            onClick = { /* TODO: Open maps */ }
        )
    }
}

