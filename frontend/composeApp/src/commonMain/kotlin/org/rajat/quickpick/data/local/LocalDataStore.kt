package org.rajat.quickpick.data.local

interface LocalDataStore {

    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()

    suspend fun saveRefreshToken(token: String)
    suspend fun getRefreshToken(): String?
    suspend fun clearRefreshToken()

    suspend fun clearAll()
}