package org.rajat.quickpick.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
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
        private val USER_ROLE_KEY = stringPreferencesKey("user_role")
        private val VENDOR_PROFILE_KEY = stringPreferencesKey("vendor_profile")
        private val USER_PROFILE_KEY = stringPreferencesKey("user_profile")

        private val HAS_ONBOARDED_KEY = booleanPreferencesKey("has_onboarded")

        private val HAS_REQUESTED_NOTIFICATION_PERMISSION_KEY = booleanPreferencesKey("has_requested_notification_permission")

        private val PENDING_VERIFICATION_EMAIL_KEY = stringPreferencesKey("pending_verification_email")
        private val PENDING_VERIFICATION_USER_TYPE_KEY = stringPreferencesKey("pending_verification_user_type")

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

    override suspend fun saveUserRole(role: String) {
        dataStore.edit { it[USER_ROLE_KEY] = role }
    }

    override suspend fun getUserRole(): String? =
        dataStore.data.first()[USER_ROLE_KEY]

    override fun getUserRoleFlow(): Flow<String?> =
        dataStore.data.map { it[USER_ROLE_KEY] }.distinctUntilChanged()

    override suspend fun clearUserRole() {
        dataStore.edit { it.remove(USER_ROLE_KEY) }
    }

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

    override suspend fun clearVendorProfile() {
        dataStore.edit { it.remove(VENDOR_PROFILE_KEY) }
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

    override suspend fun clearUserProfile() {
        dataStore.edit { it.remove(USER_PROFILE_KEY) }
    }

    override suspend fun setHasOnboarded(bool: Boolean) {
        dataStore.edit { prefs ->
            prefs[HAS_ONBOARDED_KEY] = bool
        }
    }

    override val hasOnboarded: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[HAS_ONBOARDED_KEY] ?: false
        }

    override suspend fun setHasRequestedNotificationPermission(bool: Boolean) {
        dataStore.edit { prefs ->
            prefs[HAS_REQUESTED_NOTIFICATION_PERMISSION_KEY] = bool
        }
    }

    override suspend fun getHasRequestedNotificationPermission(): Boolean =
        dataStore.data.first()[HAS_REQUESTED_NOTIFICATION_PERMISSION_KEY] ?: false

    override suspend fun savePendingVerification(email: String, userType: String) {
        dataStore.edit { prefs ->
            prefs[PENDING_VERIFICATION_EMAIL_KEY] = email
            prefs[PENDING_VERIFICATION_USER_TYPE_KEY] = userType
        }
    }

    override suspend fun getPendingVerificationEmail(): String? =
        dataStore.data.first()[PENDING_VERIFICATION_EMAIL_KEY]

    override suspend fun getPendingVerificationUserType(): String? =
        dataStore.data.first()[PENDING_VERIFICATION_USER_TYPE_KEY]

    override suspend fun clearPendingVerification() {
        dataStore.edit { prefs ->
            prefs.remove(PENDING_VERIFICATION_EMAIL_KEY)
            prefs.remove(PENDING_VERIFICATION_USER_TYPE_KEY)
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
