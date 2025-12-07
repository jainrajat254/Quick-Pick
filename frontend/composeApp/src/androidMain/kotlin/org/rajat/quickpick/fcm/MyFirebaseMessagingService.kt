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
        private const val TAG = "FCMDEBUG"
    }

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val dataStore: LocalDataStore? by lazy {
        try {
            Log.d(TAG, "attempting to get datastore from koin")
            val ds = getKoin().getOrNull<LocalDataStore>()
            Log.d(TAG, "datastore obtained: ${ds != null}")
            ds
        } catch (e: Exception) {
            Log.w(TAG, "koin not initialized yet, cannot get datastore: ${e.message}")
            null
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "firebase messaging service oncreate called")
        FcmPlatformManager.init(applicationContext)
        Log.d(TAG, "service initialized")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "new fcm token received")
        Log.d(TAG, "new fcm token: ${token.take(30)}...")

        val localDataStore = dataStore
        if (localDataStore == null) {
            Log.w(TAG, "datastore not available, fcm token will be sent after login")
            return
        }

        serviceScope.launch {
            Log.d(TAG, "checking if user is logged in")
            val authToken = localDataStore.getToken()
            if (authToken != null) {
                Log.d(TAG, "user is logged in, auth token: ${authToken.take(20)}...")
                Log.d(TAG, "sending new fcm token to server")
                FcmPlatformManager.sendTokenToServer(token, authToken)
            } else {
                Log.d(TAG, "user not logged in, fcm token will be sent after login")
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "new message received")
        Log.d(TAG, "message from: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "message has data payload")
            Log.d(TAG, "message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        } else {
            Log.d(TAG, "message has no data payload")
        }

        remoteMessage.notification?.let { notification ->
            Log.d(TAG, "message has notification payload")
            Log.d(TAG, "notification title: ${notification.title}")
            Log.d(TAG, "notification body: ${notification.body}")

            val orderId = remoteMessage.data["orderId"] ?: "unknown"
            val title = notification.title ?: "New Order"
            val message = notification.body ?: "You have a new order"
            val type = remoteMessage.data["type"] ?: "NEW_ORDER"

            Log.d(TAG, "showing notification: orderId=$orderId, type=$type")
            NotificationHelper.showOrderNotification(
                context = applicationContext,
                orderId = orderId,
                title = title,
                message = message,
                type = type
            )
            Log.d(TAG, "notification shown successfully")
        } ?: run {
            Log.d(TAG, "message has no notification payload")
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        Log.d(TAG, "handle data message called")
        val orderId = data["orderId"] ?: run {
            Log.w(TAG, "received fcm data message without orderId")
            return
        }
        val type = data["type"] ?: "NEW_ORDER"
        val title = data["title"] ?: "New Order"
        val message = data["message"] ?: "You have a new order"

        Log.d(TAG, "handling order notification: orderId=$orderId, type=$type, title=$title, message=$message")

        NotificationHelper.showOrderNotification(
            context = applicationContext,
            orderId = orderId,
            title = title,
            message = message,
            type = type
        )
        Log.d(TAG, "data message notification shown successfully")
    }
}
