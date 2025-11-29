package org.rajat.quickpick.fcm

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.rajat.quickpick.utils.notifications.NotificationHelper

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFcmService"
    }

    override fun onCreate() {
        super.onCreate()
        FcmPlatformManager.init(applicationContext)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")

        getSharedPreferences("QuickPickPrefs", Context.MODE_PRIVATE).edit {
            putString("fcm_token", token)
        }

        FcmPlatformManager.sendTokenToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message from: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "Message notification: ${notification.title} - ${notification.body}")

            val orderId = remoteMessage.data["orderId"] ?: "unknown"
            val title = notification.title ?: "New Order"
            val message = notification.body ?: "You have a new order"
            val type = remoteMessage.data["type"] ?: "NEW_ORDER"

            NotificationHelper.showOrderNotification(
                context = applicationContext,
                orderId = orderId,
                title = title,
                message = message,
                type = type
            )
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val orderId = data["orderId"] ?: run {
            Log.w(TAG, "Received FCM data message without orderId")
            return
        }
        val type = data["type"] ?: "NEW_ORDER"
        val title = data["title"] ?: "New Order"
        val message = data["message"] ?: "You have a new order"

        Log.d(TAG, "Handling order notification: $orderId - $type")

        NotificationHelper.showOrderNotification(
            context = applicationContext,
            orderId = orderId,
            title = title,
            message = message,
            type = type
        )
    }
}
