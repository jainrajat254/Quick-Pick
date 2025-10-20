package org.rajat.quickpick.utils.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom

suspend inline fun <reified T> HttpClient.safeDelete(
    endpoint: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap()
): T {
    try {
        val response: HttpResponse = delete {
            url {
                takeFrom(endpoint)
                queryParams.forEach { (key, value) ->
                    parameters.append(key, value)
                }
            }
            headers.forEach { (key, value) ->
                header(key, value)
            }
        }

        if (!response.status.isSuccess()) {
            val errorBody = response.bodyAsText()
            val errorMessage = extractErrorMessage(errorBody)
            throw Exception(errorMessage)
        }

        return response.body()
    } catch (e: Exception) {
        throw e
    }
}
