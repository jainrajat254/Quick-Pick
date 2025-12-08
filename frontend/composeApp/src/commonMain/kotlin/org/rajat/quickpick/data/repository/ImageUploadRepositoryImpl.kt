package org.rajat.quickpick.data.repository

import org.rajat.quickpick.domain.repository.ImageUploadRepository
import org.rajat.quickpick.domain.service.ImageUploadApiService
import co.touchlab.kermit.Logger

private val logger = Logger.withTag("CLOUDINARY_IMAGE_DEBUG")

class ImageUploadRepositoryImpl(
    private val imageUploadApiService: ImageUploadApiService
) : ImageUploadRepository {

    override suspend fun uploadImage(imageBytes: ByteArray, fileName: String): Result<String> {
        logger.d { "ImageUploadRepository: uploadImage called with fileName=$fileName, size=${imageBytes.size} bytes" }
        return try {
            logger.d { "ImageUploadRepository: Calling API service uploadImage" }
            val imageUrl = imageUploadApiService.uploadImage(imageBytes, fileName)
            logger.d { "ImageUploadRepository: API service returned URL=$imageUrl" }
            Result.success(imageUrl)
        } catch (e: Exception) {
            logger.e(e) { "ImageUploadRepository: Upload failed with exception=${e.message}" }
            Result.failure(e)
        }
    }
}
