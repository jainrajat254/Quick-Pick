package org.rajat.quickpick

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.rajat.quickpick.di.initializeKoin
import org.rajat.quickpick.utils.tokens.initScheduler

class QuickPickApplication : Application() {
    companion object {
        lateinit var appContext: Context
        fun provideAppContext(): Context = appContext
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initScheduler(this)
        initializeKoin {
            androidContext(this@QuickPickApplication)
        }
    }
}