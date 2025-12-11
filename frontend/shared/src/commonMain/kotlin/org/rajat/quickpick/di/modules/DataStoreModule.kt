package org.rajat.quickpick.di.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.dsl.module
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.data.local.LocalDataStoreImpl

val dataStoreModule = module {
    single<LocalDataStore> { LocalDataStoreImpl(get<DataStore<Preferences>>()) }
}