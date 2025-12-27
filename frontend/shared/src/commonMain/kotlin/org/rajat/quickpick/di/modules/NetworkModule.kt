package org.rajat.quickpick.di.modules

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.koin.mp.KoinPlatform.getKoin
import org.rajat.quickpick.di.SessionManager
import org.rajat.quickpick.di.TokenProvider
import org.rajat.quickpick.di.getKtorEngine
import org.rajat.quickpick.utils.tokens.RefreshTokenManager


private val logger = Logger.withTag("Auth")

val AuthHeaderPlugin = createClientPlugin("AuthHeaderPlugin") {
    onRequest { request, _ ->
        logger.d { "AuthHeaderPlugin: applying auth header" }

        val token = TokenProvider.token
        val excludedPaths = listOf(
            "/request-otp", "/verify-otp", "/auth/users"
        )

        when {
            excludedPaths.any { request.url.encodedPath.endsWith(it) } -> {
                logger.d { "AuthHeaderPlugin: excluded path" }
            }

            !token.isNullOrBlank() -> {
                request.headers.append(HttpHeaders.Authorization, "Bearer $token")
            }

            else -> {
                logger.d { "AuthHeaderPlugin: no token available" }
            }
        }
    }
}
val RefreshOn401Plugin = createClientPlugin("RefreshOn401Plugin") {

    var isRefreshing = false

    on(Send) { requestBuilder ->
        val response = proceed(requestBuilder)

        if (response.response.status.value != 401) {
            return@on response
        }

        if (isRefreshing) {
            return@on response
        }

        logger.d { "401 detected → trying refresh" }
        isRefreshing = true

        val refreshManager: RefreshTokenManager = getKoin().get()
        val refreshed = refreshManager.refreshNow()

        if (!refreshed) {
            logger.d { "Refresh FAILED → Logging out" }
            val sessionManager: SessionManager = getKoin().get()
            sessionManager.logout()

            isRefreshing = false
//            throw IllegalStateException("Refresh token expired — force logout")
        }

        logger.d { "Refresh successful → retrying request" }

        val newToken = TokenProvider.token ?: return@on response
        requestBuilder.headers.remove(HttpHeaders.Authorization)
        requestBuilder.headers.append(HttpHeaders.Authorization, "Bearer $newToken")

        isRefreshing = false
        return@on proceed(requestBuilder)
    }
}

fun provideHttpClient(): HttpClient {
    logger.d { "Initializing HttpClient" }

    return HttpClient(getKtorEngine()) {

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 90_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 90_000
        }
        install(AuthHeaderPlugin)
        install(RefreshOn401Plugin)
    }
}

val networkModule = module {
    single<HttpClient> { provideHttpClient() }
    single { RefreshTokenManager(get(), get()) }
    single { SessionManager(get()) }
}
