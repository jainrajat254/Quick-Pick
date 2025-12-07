package org.rajat.quickpick.utils.session

import co.touchlab.kermit.Logger
import org.rajat.quickpick.data.local.LocalDataStore
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.domain.modal.auth.LoginUserResponse
import org.rajat.quickpick.domain.modal.auth.LoginVendorResponse
import org.rajat.quickpick.fcm.FcmPlatformManager
import org.rajat.quickpick.utils.websocket.VendorWebSocketManager
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object AuthSessionSaver {
    @OptIn(ExperimentalTime::class)
    suspend fun saveUserSession(dataStore: LocalDataStore, response: LoginUserResponse) {
        val logger = Logger.withTag("LOGOUT_DEBUG")
        val themeLogger = Logger.withTag("ThemeLogs")
        val fcmLogger = Logger.withTag("FCMDEBUG")
        logger.d { "AuthSessionSaver - saveUserSession called" }
        val tokens = response.tokens
        if (tokens == null) {
            logger.d { "AuthSessionSaver - User tokens are null; skipping session save" }
            return
        }
        logger.d { "AuthSessionSaver - Saving user data to datastore" }

        TokenProvider.token = tokens.accessToken
        dataStore.saveToken(tokens.accessToken)
        dataStore.saveRefreshToken(tokens.refreshToken)
        val expiryMillis = Clock.System.now().toEpochMilliseconds() + (tokens.expiresIn * 1000)
        dataStore.saveTokenExpiryMillis(expiryMillis)
        dataStore.saveId(response.userId)
        dataStore.saveUserRole("USER")
        themeLogger.d { "AuthSessionSaver - Saved user role as USER (Student)" }
        dataStore.saveUserProfile(response)
        dataStore.clearVendorProfile()

        logger.d { "AuthSessionSaver - User session saved successfully" }

        fcmLogger.d { "user login - registering fcm token" }
        fcmLogger.d { "access token: ${tokens.accessToken.take(20)}..." }
        logger.d { "AuthSessionSaver - Registering FCM token for user" }
        FcmPlatformManager.initializeAndSendToken(tokens.accessToken)
        fcmLogger.d { "fcm registration initiated for user" }
    }

    @OptIn(ExperimentalTime::class)
    suspend fun saveVendorSession(dataStore: LocalDataStore, response: LoginVendorResponse) {
        val logger = Logger.withTag("LOGOUT_DEBUG")
        val themeLogger = Logger.withTag("ThemeLogs")
        val fcmLogger = Logger.withTag("FCMDEBUG")
        logger.d { "AuthSessionSaver - saveVendorSession called" }
        val tokens = response.tokens
        if (tokens == null) {
            logger.d { "AuthSessionSaver - Vendor tokens are null; skipping session save" }
            return
        }
        logger.d { "AuthSessionSaver - Saving vendor data to datastore" }

        TokenProvider.token = tokens.accessToken
        dataStore.saveToken(tokens.accessToken)
        dataStore.saveRefreshToken(tokens.refreshToken)
        val expiryMillis = Clock.System.now().toEpochMilliseconds() + (tokens.expiresIn * 1000)
        dataStore.saveTokenExpiryMillis(expiryMillis)
        dataStore.saveId(response.userId)
        dataStore.saveUserRole("VENDOR")
        themeLogger.d { "AuthSessionSaver - Saved user role as VENDOR" }
        dataStore.saveVendorProfile(response)
        dataStore.clearUserProfile()

        logger.d { "AuthSessionSaver - Vendor session saved successfully" }

        fcmLogger.d { "vendor login - registering fcm token" }
        fcmLogger.d { "access token: ${tokens.accessToken.take(20)}..." }
        logger.d { "AuthSessionSaver - Registering FCM token for vendor" }
        FcmPlatformManager.initializeAndSendToken(tokens.accessToken)
        fcmLogger.d { "fcm registration initiated for vendor" }

        logger.d { "AuthSessionSaver - Connecting WebSocket for vendor" }
        VendorWebSocketManager.connect(tokens.accessToken, "VENDOR")
    }
}
