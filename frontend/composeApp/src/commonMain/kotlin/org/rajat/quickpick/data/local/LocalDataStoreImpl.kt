package org.rajat.quickpick.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class LocalDataStoreImpl(
    private val dataStore: DataStore<Preferences>
) : LocalDataStore {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = token
        }
    }

    override suspend fun getToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[ACCESS_TOKEN_KEY]
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
        }
    }

    override suspend fun saveRefreshToken(token: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = token
        }
    }

    override suspend fun getRefreshToken(): String? {
        val preferences = dataStore.data.first()
        return preferences[REFRESH_TOKEN_KEY]
    }

    override suspend fun clearRefreshToken() {
        dataStore.edit { preferences ->
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}