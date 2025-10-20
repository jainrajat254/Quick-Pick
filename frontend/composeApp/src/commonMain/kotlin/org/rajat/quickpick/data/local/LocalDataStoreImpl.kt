package org.rajat.quickpick.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class LocalDataStoreImpl(
    private val dataStore: DataStore<Preferences>
) : LocalDataStore {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val KEY_TOKEN_EXPIRY = longPreferencesKey("token_expiry_time")
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

    override suspend fun saveTokenExpiryMillis(expiryMillis: Long) {
        dataStore.edit { it[KEY_TOKEN_EXPIRY] = expiryMillis }
    }

    override suspend fun getTokenExpiryMillis(): Long? =
        dataStore.data.map { it[KEY_TOKEN_EXPIRY] }
            .distinctUntilChanged()
            .firstOrNull()

    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}