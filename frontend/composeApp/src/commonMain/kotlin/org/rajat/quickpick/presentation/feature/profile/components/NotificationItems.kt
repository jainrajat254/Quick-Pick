package org.rajat.quickpick.presentation.feature.profile.components

import androidx.compose.runtime.Composable

@Composable
fun NotificationItems(
    generalNotification: Boolean,
    onGeneralNotificationChange: (Boolean) -> Unit,
    sound: Boolean,
    onSoundChange: (Boolean) -> Unit,
    soundCall: Boolean,
    onSoundCallChange: (Boolean) -> Unit,
    vibrate: Boolean,
    onVibrateChange: (Boolean) -> Unit,
    specialOffers: Boolean,
    onSpecialOffersChange: (Boolean) -> Unit,
    payments: Boolean,
    onPaymentsChange: (Boolean) -> Unit,
    promoDiscount: Boolean,
    onPromoDiscountChange: (Boolean) -> Unit,
    cashback: Boolean,
    onCashbackChange: (Boolean) -> Unit
){
    NotificationSettingItem(
        label = "General Notification",
        checked = generalNotification,
        onCheckedChange = { onGeneralNotificationChange }
    )
    NotificationSettingItem(
        label = "Sound",
        checked = sound,
        onCheckedChange = { onSoundChange}
    )
    NotificationSettingItem(
        label = "Sound Call",
        checked = soundCall,
        onCheckedChange = {onSoundCallChange }
    )
    NotificationSettingItem(
        label = "Vibrate",
        checked = vibrate,
        onCheckedChange = { onVibrateChange }
    )
    NotificationSettingItem(
        label = "Special Offers",
        checked = specialOffers,
        onCheckedChange = { onSpecialOffersChange }
    )
    NotificationSettingItem(
        label = "Payments",
        checked = payments,
        onCheckedChange = { onPaymentsChange }
    )
    NotificationSettingItem(
        label = "Promo and discount",
        checked = promoDiscount,
        onCheckedChange = { onPromoDiscountChange }
    )
    NotificationSettingItem(
        label = "Cashback",
        checked = cashback,
        onCheckedChange = { onCashbackChange }
    )
}
