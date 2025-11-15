package org.rajat.quickpick.utils.session

import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object AuthSessionSaver {
    @OptIn(ExperimentalTime::class)
    suspend fun saveUserSession(dataStore: LocalDataStore, response: LoginUserResponse) {
        TokenProvider.token = response.tokens.accessToken
        dataStore.saveToken(response.tokens.accessToken)
        dataStore.saveRefreshToken(response.tokens.refreshToken)
        val expiryMillis = Clock.System.now().toEpochMilliseconds() + (response.tokens.expiresIn * 1000)
        dataStore.saveTokenExpiryMillis(expiryMillis)
        dataStore.saveId(response.userId)
        dataStore.saveUserRole("USER")
        dataStore.saveUserProfile(response)
        dataStore.clearVendorProfile()
    }

    @OptIn(ExperimentalTime::class)
    suspend fun saveVendorSession(dataStore: LocalDataStore, response: LoginVendorResponse) {
        TokenProvider.token = response.tokens.accessToken
        dataStore.saveToken(response.tokens.accessToken)
        dataStore.saveRefreshToken(response.tokens.refreshToken)
        val expiryMillis = Clock.System.now().toEpochMilliseconds() + (response.tokens.expiresIn * 1000)
        dataStore.saveTokenExpiryMillis(expiryMillis)
        dataStore.saveId(response.userId)
        dataStore.saveUserRole("VENDOR")
        dataStore.saveVendorProfile(response)
        dataStore.clearUserProfile()
    }
}

