package org.rajat.quickpick.utils.permissions

interface NotificationPermissionHelper {
    fun shouldShowPermissionRequest(): Boolean
    fun requestNotificationPermission(onResult: (Boolean) -> Unit)
}

expect fun getNotificationPermissionHelper(): NotificationPermissionHelper

