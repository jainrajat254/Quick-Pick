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
import kotlinx.serialization.json.Json
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse

class LocalDataStoreImpl(
    private val dataStore: DataStore<Preferences>
) : LocalDataStore {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val KEY_TOKEN_EXPIRY = longPreferencesKey("token_expiry_time")

        private val ID_KEY = stringPreferencesKey("user_or_vendor_id")
        private val VENDOR_PROFILE_KEY = stringPreferencesKey("vendor_profile")
        private val USER_PROFILE_KEY = stringPreferencesKey("user_profile")

        private val json = Json { ignoreUnknownKeys = true }
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { it[ACCESS_TOKEN_KEY] = token }
    }

    override suspend fun getToken(): String? =
        dataStore.data.first()[ACCESS_TOKEN_KEY]

    override suspend fun clearToken() {
        dataStore.edit { it.remove(ACCESS_TOKEN_KEY) }
    }

    override suspend fun saveRefreshToken(token: String) {
        dataStore.edit { it[REFRESH_TOKEN_KEY] = token }
    }

    override suspend fun getRefreshToken(): String? =
        dataStore.data.first()[REFRESH_TOKEN_KEY]

    override suspend fun clearRefreshToken() {
        dataStore.edit { it.remove(REFRESH_TOKEN_KEY) }
    }

    override suspend fun saveTokenExpiryMillis(expiryMillis: Long) {
        dataStore.edit { it[KEY_TOKEN_EXPIRY] = expiryMillis }
    }

    override suspend fun getTokenExpiryMillis(): Long? =
        dataStore.data.map { it[KEY_TOKEN_EXPIRY] }
            .distinctUntilChanged()
            .firstOrNull()

    override suspend fun saveId(id: String) {
        dataStore.edit { it[ID_KEY] = id }
    }

    override suspend fun getId(): String? =
        dataStore.data.first()[ID_KEY]

    override suspend fun saveVendorProfile(loginVendorResponse: LoginVendorResponse) {
        val jsonString = json.encodeToString(loginVendorResponse)
        dataStore.edit { it[VENDOR_PROFILE_KEY] = jsonString }
    }

    override suspend fun getVendorProfile(): LoginVendorResponse? {
        val jsonString = dataStore.data.first()[VENDOR_PROFILE_KEY] ?: return null
        return runCatching {
            json.decodeFromString<LoginVendorResponse>(jsonString)
        }.getOrNull()
    }

    override suspend fun saveUserProfile(loginUserResponse: LoginUserResponse) {
        val jsonString = json.encodeToString(loginUserResponse)
        dataStore.edit { it[USER_PROFILE_KEY] = jsonString }
    }

    override suspend fun getUserProfile(): LoginUserResponse? {
        val jsonString = dataStore.data.first()[USER_PROFILE_KEY] ?: return null
        return runCatching {
            json.decodeFromString<LoginUserResponse>(jsonString)
        }.getOrNull()
    }

    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
