package org.rajat.quickpick.utils.tokens

import co.touchlab.kermit.Logger
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.domain.modal.auth.RefreshTokenRequest
import org.rajat.quickpick.domain.repository.AuthRepository
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class RefreshTokenManager(
    private val localDataStore: LocalDataStore,
    private val authRepository: AuthRepository
) {
    private val mutex = Mutex()
    private val logger = Logger.withTag("REFRESH_TOKEN")

    suspend fun ensureValidToken(): Boolean {
        val expiry = localDataStore.getTokenExpiryMillis()
        val now = Clock.System.now().toEpochMilliseconds()

        logger.d { "Checking token validity. Now: $now, Expiry: $expiry" }

        if (expiry == null) {
            logger.w { "Token expiry not found. Need to refresh." }
            return refreshNow()
        }

        val timeLeft = expiry - now
        logger.d { "Time left until expiry: ${timeLeft / 1000} seconds (~${timeLeft / 1000 / 60} minutes)" }

        if (timeLeft > 60_000) {
            logger.i { "Token is still valid. Skipping refresh." }
            return true
        }

        logger.i { "Token is expired or near expiry. Attempting to refresh." }
        return refreshNow()
    }

    suspend fun refreshNow(): Boolean {
        return mutex.withLock {
            try {
                val refreshToken = localDataStore.getRefreshToken()
                if (refreshToken.isNullOrEmpty()) {
                    logger.e { "No refresh token available" }
                    return@withLock false
                }

                logger.i { "Calling refresh token API..." }
                val result = authRepository.refreshToken(RefreshTokenRequest(refreshToken = refreshToken))

                result.fold(
                    onSuccess = { response ->
                        TokenProvider.token = response.tokens.accessToken
                        localDataStore.saveToken(response.tokens.accessToken)
                        localDataStore.saveRefreshToken(response.tokens.refreshToken)

                        val expiryMillis = Clock.System.now().toEpochMilliseconds() + (response.tokens.expiresIn * 1000)
                        localDataStore.saveTokenExpiryMillis(expiryMillis)

                        localDataStore.saveId(response.userId)

                        logger.i { "Token refreshed successfully. New expiry: $expiryMillis" }
                        true
                    },
                    onFailure = { error ->
                        logger.e { "Failed to refresh token: ${error.message}" }
                        false
                    }
                )
            } catch (e: Exception) {
                logger.e(e) { "Exception during token refresh" }
                false
            }
        }
    }
}
