package org.rajat.quickpick.fcm

import android.content.Context
import android.provider.Settings
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
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

    private const val TAG = "FCMDEBUG"
    private val BASE_URL = Constants.BASE_URL

    private var appContext: Context? = null

    fun init(context: Context) {
        Log.d(TAG, "fcm platform manager init called")
        appContext = context.applicationContext
        Log.d(TAG, "app context initialized: ${appContext != null}")
    }

    actual fun initializeAndSendToken(authToken: String) {
        Log.d(TAG, "initialize and send token called with auth token: ${authToken.take(20)}...")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d(TAG, "requesting fcm token from firebase")
                val token = fetchTokenWithRetry()
                if (token != null) {
                    Log.d(TAG, "fcm token obtained successfully: ${token.take(20)}...")
                    sendTokenToServerInternal(token, authToken)
                } else {
                    Log.e(TAG, "failed to obtain fcm token after retries")
                }
            } catch (e: Exception) {
                Log.e(TAG, "error getting fcm token: ${e.message}", e)
            }
        }
    }

    private suspend fun fetchTokenWithRetry(): String? {
        val maxAttempts = 5
        var attempt = 0
        var delayMs = 2000L
        while (attempt < maxAttempts) {
            attempt++
            try {
                Log.d(TAG, "fetchTokenWithRetry - attempt $attempt")
                val token = FirebaseMessaging.getInstance().token.await()
                return token
            } catch (e: Exception) {
                val msg = e.message ?: e.toString()
                Log.w(TAG, "fetchTokenWithRetry attempt $attempt failed: $msg")
                if (attempt >= maxAttempts) {
                    Log.e(TAG, "fetchTokenWithRetry - exhausted attempts: $attempt")
                    return null
                }
                try {
                    Log.d(TAG, "fetchTokenWithRetry - delaying ${delayMs}ms before next attempt")
                    delay(delayMs)
                } catch (ie: InterruptedException) {
                    Log.e(TAG, "fetchTokenWithRetry sleep interrupted", ie)
                    return null
                }
                delayMs = (delayMs * 2).coerceAtMost(30000L)
            }
        }
        return null
    }

    actual fun sendTokenToServer(fcmToken: String, authToken: String) {
        Log.d(TAG, "send token to server called - fcm token: ${fcmToken.take(20)}..., auth token: ${authToken.take(20)}...")
        CoroutineScope(Dispatchers.IO).launch {
            sendTokenToServerInternal(fcmToken, authToken)
        }
    }

    private fun sendTokenToServerInternal(fcmToken: String, authToken: String) {
        Log.d(TAG, "starting to send token to server")
        val context = appContext ?: run {
            Log.e(TAG, "error: app context not initialized")
            return
        }

        try {
            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val deviceName = "${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}"
            Log.d(TAG, "device info: id=$deviceId, name=$deviceName")

            val json = JSONObject().apply {
                put("fcmToken", fcmToken)
                put("deviceId", deviceId)
                put("deviceName", deviceName)
            }
            Log.d(TAG, "json payload prepared: ${json.toString()}")

            val requestBody = json.toString().toRequestBody("application/json".toMediaType())

            val client = OkHttpClient()
            val url = "$BASE_URL/api/fcm/token"
            Log.d(TAG, "sending post request to: $url")

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $authToken")
                .post(requestBody)
                .build()

            Log.d(TAG, "executing http request")
            client.newCall(request).execute().use { response ->
                Log.d(TAG, "response received: code=${response.code}, success=${response.isSuccessful}")
                if (response.isSuccessful) {
                    Log.d(TAG, "fcm token sent to server successfully")
                } else {
                    Log.e(TAG, "failed to send fcm token: ${response.code}, body=${response.body?.string()}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "exception sending fcm token to server: ${e.message}", e)
        }
    }

    actual fun removeTokenFromServer(authToken: String) {
        Log.d(TAG, "remove token from server called with auth token: ${authToken.take(20)}...")
        CoroutineScope(Dispatchers.IO).launch {
            removeTokenFromServerInternal(authToken)
        }
    }

    private fun removeTokenFromServerInternal(authToken: String) {
        Log.d(TAG, "starting to remove token from server")
        val context = appContext ?: run {
            Log.e(TAG, "error: app context not initialized for token removal")
            return
        }

        try {

            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            Log.d(TAG, "removing token for device: $deviceId")

            val json = JSONObject().apply {
                put("deviceId", deviceId)
            }

            val requestBody = json.toString().toRequestBody("application/json".toMediaType())

            val client = OkHttpClient()
            val url = "$BASE_URL/api/fcm/token"
            Log.d(TAG, "sending delete request to: $url")

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $authToken")
                .delete(requestBody)
                .build()

            Log.d(TAG, "executing http delete request")
            client.newCall(request).execute().use { response ->
                Log.d(TAG, "delete response: code=${response.code}, success=${response.isSuccessful}")
                if (response.isSuccessful) {
                    Log.d(TAG, "fcm token removed from server successfully")
                } else {
                    Log.e(TAG, "failed to remove token: ${response.code}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "exception removing fcm token: ${e.message}", e)
        }
    }
}