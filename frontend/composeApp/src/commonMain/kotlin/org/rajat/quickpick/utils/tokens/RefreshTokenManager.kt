package org.rajat.quickpick.utils.tokens

import co.touchlab.kermit.Logger
import kotlinx.coroutines.sync.Mutex
import org.rajat.quickpick.data.local.LocalDataStore
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
        return false
    }

}
