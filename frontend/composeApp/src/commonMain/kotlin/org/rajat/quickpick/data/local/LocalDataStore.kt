package org.rajat.quickpick.data.local

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

    suspend fun saveVendorProfile(loginVendorResponse: LoginVendorResponse)
    suspend fun getVendorProfile(): LoginVendorResponse?

    suspend fun saveUserProfile(loginUserResponse: LoginUserResponse)
    suspend fun getUserProfile(): LoginUserResponse?

    suspend fun clearAll()
}