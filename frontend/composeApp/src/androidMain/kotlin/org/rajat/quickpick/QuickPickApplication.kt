package org.rajat.quickpick

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.rajat.quickpick.di.initializeKoin

class QuickPickApplication : Application() {
    companion object {
        lateinit var appContext: Context
        fun provideAppContext(): Context = appContext
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        initializeKoin {
            androidContext(this@QuickPickApplication)
        }
    }
}