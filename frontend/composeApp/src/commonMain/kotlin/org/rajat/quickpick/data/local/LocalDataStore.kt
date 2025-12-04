package org.rajat.quickpick.data.local

import kotlinx.coroutines.flow.Flow
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse

interface LocalDataStore {

    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()

    suspend fun saveRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clearRefreshToken()

    suspend fun saveTokenExpiryMillis(expiryMillis: Long)
    suspend fun getTokenExpiryMillis(): Long?

    suspend fun saveId(id: String)
    suspend fun getId(): String?

    suspend fun saveUserRole(role: String)
    suspend fun getUserRole(): String?
    fun getUserRoleFlow(): Flow<String?>
    suspend fun clearUserRole()

    suspend fun saveVendorProfile(loginVendorResponse: LoginVendorResponse)
    suspend fun getVendorProfile(): LoginVendorResponse?
    suspend fun clearVendorProfile()

    suspend fun saveUserProfile(loginUserResponse: LoginUserResponse)
    suspend fun getUserProfile(): LoginUserResponse?
    suspend fun clearUserProfile()

    suspend fun setHasOnboarded(bool: Boolean)
    val hasOnboarded: Flow<Boolean>

    suspend fun savePendingVerification(email: String, userType: String)
    suspend fun getPendingVerificationEmail(): String?
    suspend fun getPendingVerificationUserType(): String?
    suspend fun clearPendingVerification()

    suspend fun clearAll()
}