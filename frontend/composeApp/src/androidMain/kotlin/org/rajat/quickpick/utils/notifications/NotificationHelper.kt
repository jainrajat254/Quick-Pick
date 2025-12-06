package org.rajat.quickpick.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import org.rajat.quickpick.MainActivity

object NotificationHelper {

    private const val TAG = "FCMDEBUG"
    private const val CHANNEL_ID_ORDERS = "orders"
    private const val CHANNEL_NAME_ORDERS = "Order Updates"
    private const val CHANNEL_DESC_ORDERS = "Notifications for new orders and order updates"

    fun createNotificationChannels(context: Context) {
        Log.d(TAG, "create notification channels called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "android o+, creating notification channels")
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val orderChannel = NotificationChannel(
                CHANNEL_ID_ORDERS,
                CHANNEL_NAME_ORDERS,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC_ORDERS
                enableVibration(true)
                enableLights(true)
            }

            notificationManager.createNotificationChannel(orderChannel)
            Log.d(TAG, "order notification channel created: $CHANNEL_ID_ORDERS")
        } else {
            Log.d(TAG, "android < o, notification channels not needed")
        }
    }

    fun showOrderNotification(
        context: Context,
        orderId: String,
        title: String,
        message: String,
        type: String = "NEW_ORDER"
    ) {
        Log.d(TAG, "show order notification called")
        Log.d(TAG, "orderId: $orderId, title: $title, message: $message, type: $type")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("orderId", orderId)
            putExtra("notificationType", type)
            putExtra("openOrderDetails", true)
        }
        Log.d(TAG, "intent created with extras")

        val pendingIntent = PendingIntent.getActivity(
            context,
            orderId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d(TAG, "pending intent created")

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID_ORDERS)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        val notificationId = orderId.hashCode()
        Log.d(TAG, "showing notification with id: $notificationId")
        notificationManager.notify(notificationId, notificationBuilder.build())
        Log.d(TAG, "notification displayed successfully")
    }

    @Suppress("unused")
    fun cancelNotification(context: Context, orderId: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(orderId.hashCode())
    }

    @Suppress("unused")
    fun cancelAllNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }
}
