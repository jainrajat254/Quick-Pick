package org.rajat.quickpick.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.utils.notifications.NotificationHelper

class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    companion object {
        private const val TAG = "MyFcmService"
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val dataStore: LocalDataStore? by lazy {
        try {
            getKoin().getOrNull<LocalDataStore>()
        } catch (_: Exception) {
            Log.w(TAG, "Koin not initialized yet, cannot get DataStore")
            null
        }
    }

    override fun onCreate() {
        super.onCreate()
        FcmPlatformManager.init(applicationContext)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New FCM token: $token")

        val localDataStore = dataStore
        if (localDataStore == null) {
            Log.w(TAG, "DataStore not available, FCM token will be sent after login")
            return
        }

        serviceScope.launch {
            val authToken = localDataStore.getToken()
            if (authToken != null) {
                Log.d(TAG, "User is logged in, sending new FCM token to server")
                FcmPlatformManager.sendTokenToServer(token, authToken)
            } else {
                Log.d(TAG, "User not logged in, FCM token will be sent after login")
            }
        }
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
