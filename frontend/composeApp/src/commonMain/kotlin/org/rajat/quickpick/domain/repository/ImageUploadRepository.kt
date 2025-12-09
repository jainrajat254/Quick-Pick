package org.rajat.quickpick.domain.repository

interface ImageUploadRepository {
    suspend fun uploadImage(imageBytes: ByteArray, fileName: String): Result<String>
}

