package org.rajat.quickpick.utils.permissions

actual fun getNotificationPermissionHelper(): NotificationPermissionHelper {
    return IosNotificationPermissionHelper
}

object IosNotificationPermissionHelper : NotificationPermissionHelper {
    override fun shouldShowPermissionRequest(): Boolean {
        // iOS handles notification permission differently
        // For now, we'll return false and handle iOS permission separately if needed
        return false
    }

    override fun requestNotificationPermission(onResult: (Boolean) -> Unit) {

        onResult(true)
    }
}

