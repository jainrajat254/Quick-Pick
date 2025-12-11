package org.rajat.quickpick

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.rajat.quickpick.utils.navigation.PendingNavigation

open class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "FCMDEBUG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Log.d(TAG, "MainActivity.onCreate() called")

        handleNotificationIntent(intent)

        setContent {
            App()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(TAG, "MainActivity.onNewIntent() called")
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: Intent?) {
        Log.d(TAG, "MainActivity.handleNotificationIntent() called")
        intent?.let {
            val orderId = it.getStringExtra("orderId")
            val notificationType = it.getStringExtra("notificationType")
            val openOrderDetails = it.getBooleanExtra("openOrderDetails", false)

            Log.d(TAG, "MainActivity - Intent extras: orderId=$orderId, type=$notificationType, openOrderDetails=$openOrderDetails")

            if (openOrderDetails && orderId != null) {
                Log.d(TAG, "========================================")
                Log.d(TAG, "MainActivity - Notification clicked - Opening order details")
                Log.d(TAG, "MainActivity - OrderID: $orderId")
                Log.d(TAG, "MainActivity - Type: $notificationType")
                Log.d(TAG, "========================================")

                PendingNavigation.setPendingOrder(
                    orderId = orderId,
                    type = notificationType ?: "NEW_ORDER"
                )
                Log.d(TAG, "MainActivity - Pending navigation set for order: $orderId")

                it.removeExtra("orderId")
                it.removeExtra("notificationType")
                it.removeExtra("openOrderDetails")
                Log.d(TAG, "MainActivity - Intent extras cleared")
            } else {
                Log.d(TAG, "MainActivity - No notification action needed (openOrderDetails=$openOrderDetails, orderId=$orderId)")
            }
        } ?: run {
            Log.d(TAG, "MainActivity - Intent is null, no notification to handle")
        }
    }
}
