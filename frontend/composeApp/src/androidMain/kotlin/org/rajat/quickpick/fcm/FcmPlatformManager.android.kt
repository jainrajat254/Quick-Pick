package org.rajat.quickpick.fcm

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.rajat.quickpick.utils.Constants

actual object FcmPlatformManager {

    private const val TAG = "FcmTokenManager"
    private val BASE_URL = Constants.BASE_URL

    private var appContext: Context? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    actual fun initializeAndSendToken() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                Log.d(TAG, "FCM Token obtained: $token")
                sendTokenToServerInternal(token)
            } catch (e: Exception) {
                Log.e(TAG, "Error getting FCM token", e)
            }
        }
    }

    actual fun sendTokenToServer(fcmToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sendTokenToServerInternal(fcmToken)
        }
    }

    private fun sendTokenToServerInternal(fcmToken: String) {
        val context = appContext ?: run {
            Log.e(TAG, "App context not initialized. Call FcmTokenManager.init(context) first.")
            return
        }

        try {
            val prefs = context.getSharedPreferences("QuickPickPrefs", Context.MODE_PRIVATE)
            val authToken = prefs.getString("auth_token", null) ?: run {
                Log.d(TAG, "Auth token not found — skipping sendTokenToServer")
                return
            }

            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val deviceName = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"

            val json = JSONObject().apply {
                put("fcmToken", fcmToken)
                put("deviceId", deviceId)
                put("deviceName", deviceName)
            }

            val requestBody = json.toString().toRequestBody("application/json".toMediaType())

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("$BASE_URL/api/fcm/token")
                .addHeader("Authorization", "Bearer $authToken")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.d(TAG, "FCM token sent to server successfully")
                } else {
                    Log.e(TAG, "Failed to send FCM token: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending FCM token to server", e)
        }
    }

    actual fun removeTokenFromServer() {
        CoroutineScope(Dispatchers.IO).launch {
            removeTokenFromServerInternal()
        }
    }

    private fun removeTokenFromServerInternal() {
        val context = appContext ?: run {
            Log.e(TAG, "App context not initialized. Call FcmTokenManager.init(context) first.")
            return
        }

        try {
            val prefs = context.getSharedPreferences("QuickPickPrefs", Context.MODE_PRIVATE)
            val authToken = prefs.getString("auth_token", null) ?: run {
                Log.d(TAG, "Auth token not found — skipping removeTokenFromServer")
                return
            }

            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

            val json = JSONObject().apply {
                put("deviceId", deviceId)
            }

            val requestBody = json.toString().toRequestBody("application/json".toMediaType())

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("$BASE_URL/api/fcm/token")
                .addHeader("Authorization", "Bearer $authToken")
                .delete(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.d(TAG, "FCM token removed from server")
                } else {
                    Log.e(TAG, "Failed to remove token: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error removing FCM token", e)
        }
    }
}