package org.rajat.quickpick.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.rajat.quickpick.domain.service.ImageUploadApiService
import co.touchlab.kermit.Logger
import org.rajat.quickpick.utils.Constants

private val logger = Logger.withTag("CLOUDINARY_IMAGE_DEBUG")

class ImageUploadApiServiceImpl(private val httpClient: HttpClient) : ImageUploadApiService {

    override suspend fun uploadImage(imageBytes: ByteArray, fileName: String): String {
        logger.d { "ImageUploadApiService: Starting upload - fileName=$fileName, size=${imageBytes.size} bytes" }

        try {
            logger.d { "ImageUploadApiService: Preparing multipart form data" }
            val response = httpClient.submitFormWithBinaryData(
                url = "${Constants.BASE_URL}${Constants.Endpoints.UPLOAD_IMAGE}",
                formData = formData {
                    append("file", imageBytes, Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                }
            )

            logger.d { "ImageUploadApiService: Received response - status=${response.status.value}" }

            if (response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                logger.d { "ImageUploadApiService: Response body=$responseBody" }

                return if (responseBody.startsWith("http")) {
                    logger.d { "ImageUploadApiService: Response is plain URL" }
                    responseBody
                } else {
                    logger.d { "ImageUploadApiService: Parsing response as JSON" }
                    val json = Json.parseToJsonElement(responseBody).jsonObject
                    val imageUrl = json["url"]?.jsonPrimitive?.content
                        ?: json["secure_url"]?.jsonPrimitive?.content
                        ?: throw Exception("Image URL not found in response")
                    logger.d { "ImageUploadApiService: Extracted URL from JSON=$imageUrl" }
                    imageUrl
                }
            } else {
                val errorBody = response.bodyAsText()
                logger.e { "ImageUploadApiService: Upload failed - status=${response.status.value}, error=$errorBody" }
                throw Exception("Upload failed: ${response.status.description} - $errorBody")
            }
        } catch (e: Exception) {
            logger.e(e) { "ImageUploadApiService: Exception during upload - ${e.message}" }
            throw e
        }
    }
}
