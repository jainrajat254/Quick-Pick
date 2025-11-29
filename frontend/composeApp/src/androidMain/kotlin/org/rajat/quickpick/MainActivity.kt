package org.rajat.quickpick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.rajat.quickpick.utils.navigation.PendingNavigation

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        handleNotificationIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        intent?.let {
            val orderId = it.getStringExtra("orderId")
            val notificationType = it.getStringExtra("notificationType")
            val openOrderDetails = it.getBooleanExtra("openOrderDetails", false)

            if (openOrderDetails && orderId != null) {
                Log.d(TAG, "Notification clicked - Opening order details for: $orderId (type: $notificationType)")

                PendingNavigation.setPendingOrder(
                    orderId = orderId,
                    type = notificationType ?: "NEW_ORDER"
                )

                it.removeExtra("orderId")
                it.removeExtra("notificationType")
                it.removeExtra("openOrderDetails")
            }
        }
    }
}
