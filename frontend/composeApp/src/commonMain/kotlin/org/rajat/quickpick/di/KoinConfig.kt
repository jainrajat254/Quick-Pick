package org.rajat.quickpick.di

import kotlinx.coroutines.runBlocking
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.KoinAppDeclaration
import org.rajat.quickpick.data.local.LocalDataStore

var koinApp: KoinApplication? = null
var jwtToken: String? = null

fun initializeKoin(config: KoinAppDeclaration? = null) {
    stopKoin()

    koinApp = startKoin {
        config?.invoke(this)
        modules(quickPickModules)
    }

    val sharedPrefsManager = koinApp!!.koin.get<LocalDataStore>()

    jwtToken = runBlocking {
        sharedPrefsManager.getToken()
    }
    TokenProvider.token = jwtToken

    println("JWT Token in Common: $jwtToken")
}