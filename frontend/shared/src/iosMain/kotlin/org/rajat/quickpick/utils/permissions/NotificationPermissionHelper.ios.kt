package org.rajat.quickpick.utils.permissions

actual fun getNotificationPermissionHelper(): NotificationPermissionHelper {
    return IosNotificationPermissionHelper
}

object IosNotificationPermissionHelper : NotificationPermissionHelper {
    override fun shouldShowPermissionRequest(): Boolean {
        return false
    }

    override fun requestNotificationPermission(onResult: (Boolean) -> Unit) {
        onResult(true)
    }
}

actual fun isAndroid13OrAbove(): Boolean = false
