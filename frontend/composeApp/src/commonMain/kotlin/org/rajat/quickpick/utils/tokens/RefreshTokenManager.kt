package org.rajat.quickpick.utils.tokens

import co.touchlab.kermit.Logger
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.domain.modal.auth.RefreshTokenRequest
import org.rajat.quickpick.domain.repository.AuthRepository
import kotlin.compareTo
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
            logger.d { "Attempting token refresh..." }

            val refreshToken = localDataStore.getRefreshToken()
            if (refreshToken.isNullOrBlank()) {
                logger.e { "Refresh token missing or blank. Cannot refresh." }
                return@withLock false
            }

            logger.d { "Using refresh token: $refreshToken" }

            val result = try {
                val refreshTokenRequest = RefreshTokenRequest(refreshToken = refreshToken)
                authRepository.refreshToken(refreshTokenRequest = refreshTokenRequest)
            } catch (e: Exception) {
                logger.e(e) { "Exception occurred in authRepository.refreshToken()" }
                return@withLock false
            }

            logger.d { "Received result from authRepository.refreshToken()" }

            result.fold(
                onSuccess = { response ->
                    val tokens = response.tokens
                    val accessToken = tokens.accessToken
                    val newRefresh = tokens.refreshToken
                    val expiresIn = tokens.expiresIn

                    if (accessToken.isBlank() || newRefresh.isBlank()) {
                        logger.e { "accessToken or newRefreshToken is null/blank. accessToken='$accessToken', newRefresh='$newRefresh'" }
                        return@fold false
                    }

                    val newExpiry = Clock.System.now().toEpochMilliseconds() + (expiresIn * 1000L)
                    val refreshAt = newExpiry - 60_000

                    logger.i { "Token refreshed successfully. New expiry: $newExpiry" }

                    localDataStore.saveToken(accessToken)
                    localDataStore.saveRefreshToken(newRefresh)
                    localDataStore.saveTokenExpiryMillis(newExpiry)
                    TokenProvider.token = accessToken

                    PlatformScheduler.scheduleRefreshAt(refreshAt)
                    logger.d { "Refresh scheduled at: $refreshAt" }

                    true
                },
                onFailure = { throwable ->
                    logger.e(throwable) { "Token refresh failed: ${throwable.message}" }
                    false
                }
            )
        }
    }

}
