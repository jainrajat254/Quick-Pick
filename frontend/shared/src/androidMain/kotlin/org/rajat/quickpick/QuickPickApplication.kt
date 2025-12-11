package org.rajat.quickpick

import android.app.Application
import android.content.Context
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.rajat.quickpick.di.initializeKoin
import org.rajat.quickpick.fcm.FcmPlatformManager
import org.rajat.quickpick.utils.notifications.NotificationHelper
import org.rajat.quickpick.utils.tokens.initScheduler

class QuickPickApplication : Application() {
    companion object {
        private const val TAG = "FCMDEBUG"
        lateinit var appContext: Context
        fun provideAppContext(): Context = appContext
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "quick pick application oncreate - starting app initialization")
        appContext = applicationContext

        Log.d(TAG, "initializing fcm platform manager")
        FcmPlatformManager.init(applicationContext)
        Log.d(TAG, "fcm platform manager initialized")

        Log.d(TAG, "creating notification channels")
        NotificationHelper.createNotificationChannels(applicationContext)
        Log.d(TAG, "notification channels created")

        initScheduler(this)
        initializeKoin {
            androidContext(this@QuickPickApplication)
        }
        Log.d(TAG, "app initialization complete")
    }
}