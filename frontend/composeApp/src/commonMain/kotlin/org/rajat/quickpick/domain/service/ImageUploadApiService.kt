package org.rajat.quickpick.domain.service

interface ImageUploadApiService {
    suspend fun uploadImage(imageBytes: ByteArray, fileName: String): String
}

