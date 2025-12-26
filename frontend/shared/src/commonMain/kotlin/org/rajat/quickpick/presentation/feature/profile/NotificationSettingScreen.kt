package org.rajat.quickpick.presentation.feature.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.rajat.quickpick.presentation.feature.profile.components.NotificationItems
import org.rajat.quickpick.presentation.feature.profile.components.NotificationSettingItem
import org.rajat.quickpick.presentation.theme.AppTheme

@Composable
fun NotificationSettingsScreen(
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    var generalNotification by remember { mutableStateOf(true) }
    var sound by remember { mutableStateOf(true) }
    var soundCall by remember { mutableStateOf(true) }
    var vibrate by remember { mutableStateOf(false) }
    var specialOffers by remember { mutableStateOf(false) }
    var payments by remember { mutableStateOf(false) }
    var promoDiscount by remember { mutableStateOf(false) }
    var cashback by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp)
            ,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Notification Items
        NotificationItems(
            generalNotification = generalNotification,
            onGeneralNotificationChange = { generalNotification=it },
            sound = sound,
            onSoundChange ={sound=it},
            soundCall = soundCall,
            onSoundCallChange ={soundCall=it},
            vibrate = vibrate,
            onVibrateChange = {vibrate=it},
            specialOffers = specialOffers,
            onSpecialOffersChange = {specialOffers=it},
            payments = payments,
            onPaymentsChange = {payments=it},
            promoDiscount = promoDiscount,
            onPromoDiscountChange = {promoDiscount=it},
            cashback = cashback,
            onCashbackChange = {cashback=it}
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

