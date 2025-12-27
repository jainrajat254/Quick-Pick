package org.rajat.quickpick.di

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.utils.tokens.PlatformScheduler

private val logger = Logger.withTag("SessionManager")

class SessionManager(
    private val dataStore: LocalDataStore
) {
    fun logout() {
        CoroutineScope(Dispatchers.Default).launch {
            logger.d { "SessionManager: Starting logout process" }

            try {
                PlatformScheduler.cancelScheduledRefresh()
                logger.d { "Cancelled scheduled refresh" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to cancel scheduled refresh" }
            }

            try {
                dataStore.clearPendingVerification()
                logger.d { "Cleared pending verification" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to clear pending verification" }
            }

            try {
                dataStore.clearAll()
                logger.d { "Cleared all data store" }
            } catch (e: Exception) {
                logger.e(e) { "Failed to clear data store" }
            }

            TokenProvider.token = null
            logger.i { "Logout completed successfully" }
        }
    }
}