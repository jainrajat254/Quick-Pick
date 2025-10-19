package org.rajat.quickpick.di.modules

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.di.getKtorEngine
import org.rajat.quickpick.utils.tokens.RefreshTokenManager

private val logger = Logger.withTag("REFRESH_TOKEN")

val AuthHeaderPlugin = createClientPlugin("AuthHeaderPlugin") {
    onRequest { request, _ ->
        val refreshManager: RefreshTokenManager = getKoin().get()

        logger.d { "Intercepting request: ${request.url.encodedPath}" }

        val refreshed = try {
            withTimeout(5000L) {
                refreshManager.ensureValidToken()
            }
        } catch (e: Exception) {
            logger.e(e) { "Timeout or exception in ensureValidToken()" }
            false
        }

        if (refreshed) {
            logger.d { "Token valid or refreshed successfully before request." }
        } else {
            logger.w { "Token refresh failed or not valid. Proceeding without valid token." }
        }

        val token = TokenProvider.token
        val excludedPaths = listOf("/request-otp", "/verify-otp", "/auth/users")

        when {
            excludedPaths.any { request.url.encodedPath.endsWith(it) } -> {
                logger.i { "Skipping auth header for excluded path: ${request.url.encodedPath}" }
            }

            !token.isNullOrBlank() -> {
                request.headers.append(HttpHeaders.Authorization, "Bearer $token")
                logger.d { "Authorization header added for: ${request.url.encodedPath}" }
            }

            else -> {
                logger.w { "No token available to attach in Authorization header." }
            }
        }
    }
}

fun provideHttpClient(): HttpClient {
    return HttpClient(getKtorEngine()) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 45_000
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(AuthHeaderPlugin)
    }
}

val networkModule = module {
    single<HttpClient> { provideHttpClient() }
    single { RefreshTokenManager(get(), get()) }
}
