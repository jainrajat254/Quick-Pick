package org.rajat.quickpick.utils.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom

suspend inline fun <reified T> HttpClient.safeGet(
    endpoint: String,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap()
): T {
    try {
        val response: HttpResponse = get {
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

suspend inline fun <reified T> HttpClient.safeGetDemographic(
    endpoint: String,
    queryParams: Map<String, List<String>>,
    headers: Map<String, String> = emptyMap()
): T {
    try {
        val response: HttpResponse = get {
            url {
                takeFrom(endpoint)
                queryParams.forEach { (key, values) ->
                    values.forEach { value ->
                        parameters.append(key, value)
                    }
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