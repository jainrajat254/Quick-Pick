package org.rajat.quickpick.utils.tokens

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import org.koin.mp.KoinPlatform.getKoin
import java.util.concurrent.TimeUnit

private lateinit var appContext: Context

fun initScheduler(context: Context) {
    appContext = context.applicationContext
}

actual object PlatformScheduler {
    actual fun scheduleRefreshAt(triggerAtMillis: Long) {
        val delay = (triggerAtMillis - System.currentTimeMillis()).coerceAtLeast(0L)
        val work = OneTimeWorkRequestBuilder<RefreshTokenWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(appContext)
            .enqueueUniqueWork("refresh_token", ExistingWorkPolicy.REPLACE, work)
    }

    actual fun cancelScheduledRefresh() {
        WorkManager.getInstance(appContext).cancelUniqueWork("refresh_token")
    }
}

class RefreshTokenWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        val refreshManager: RefreshTokenManager = getKoin().get()
        return if (refreshManager.refreshNow()) Result.success() else Result.retry()
    }
}