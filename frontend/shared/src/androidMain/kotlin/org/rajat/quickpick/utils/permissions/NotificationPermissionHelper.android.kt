package org.rajat.quickpick.utils.permissions

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import co.touchlab.kermit.Logger

actual fun getNotificationPermissionHelper(): NotificationPermissionHelper {
    return AndroidNotificationPermissionHelper
}

object AndroidNotificationPermissionHelper : NotificationPermissionHelper {
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    override fun shouldShowPermissionRequest(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    override fun requestNotificationPermission(onResult: (Boolean) -> Unit) {
        onResult(true)
    }
}

@Composable
fun rememberNotificationPermissionState(
    onPermissionResult: (Boolean) -> Unit
): NotificationPermissionState {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        onPermissionResult(isGranted)
    }

    return remember {
        NotificationPermissionState(
            context = context,
            permissionLauncher = { permission ->
                permissionLauncher.launch(permission)
            }
        )
    }
}

class NotificationPermissionState(
    private val context: Context,
    private val permissionLauncher: (String) -> Unit
) {
    private val logger = Logger.withTag("NotificationImplementation")

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
    fun shouldShowPermissionRequest(): Boolean {
        val shouldShow = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        logger.d { "shouldShowPermissionRequest: $shouldShow, SDK_INT: ${Build.VERSION.SDK_INT}" }
        return shouldShow
    }

    fun isPermissionGranted(): Boolean {
        val isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        logger.d { "isPermissionGranted: $isGranted" }
        return isGranted
    }

    fun requestPermission() {
        logger.d { "requestPermission called, SDK_INT: ${Build.VERSION.SDK_INT}" }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            logger.d { "Launching permission request for POST_NOTIFICATIONS" }
            permissionLauncher(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            logger.d { "Permission not needed for SDK < 33" }
        }
    }

    fun shouldShowRationale(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val activity = context.findActivity()
            val result = activity?.shouldShowRequestPermissionRationale(
                Manifest.permission.POST_NOTIFICATIONS
            ) ?: false
            logger.d { "shouldShowRationale: $result, activity found: ${activity != null}" }
            return result
        }
        logger.d { "shouldShowRationale: false (SDK < 33)" }
        return false
    }


    fun openAppSettings() {
        logger.d { "openAppSettings called" }
        logger.d { "Context package name: ${context.packageName}" }

        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            logger.d { "Starting app settings intent: $intent" }
            logger.d { "Intent action: ${intent.action}, data: ${intent.data}, flags: ${intent.flags}" }
            context.startActivity(intent)
            logger.i { "Successfully launched app settings" }
        } catch (e: Exception) {
            logger.e(e) { "Failed to open app settings, trying fallback" }
            try {
                val intent = Intent(Settings.ACTION_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                logger.d { "Starting fallback settings intent" }
                context.startActivity(intent)
                logger.i { "Successfully launched general settings" }
            } catch (e2: Exception) {
                logger.e(e2) { "Failed to open any settings screen" }
            }
        }
    }
}

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

