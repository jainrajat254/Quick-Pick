package org.rajat.quickpick.utils.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.takeFrom

suspend inline fun <reified T, reified R> HttpClient.safePut(
    endpoint: String,
    body: R,
    queryParams: Map<String, String> = emptyMap(),
    headers: Map<String, String> = emptyMap()
): T {
    try {
        val response: HttpResponse = put {
            url {
                takeFrom(endpoint)
                queryParams.forEach { (key, value) ->
                    parameters.append(key, value)
                }
            }
            headers.forEach { (key, value) ->
                header(key, value)
            }
            contentType(ContentType.Application.Json)
            setBody(body)
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
