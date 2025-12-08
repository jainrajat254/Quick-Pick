package org.rajat.quickpick.utils

object ImageValidator {
    private const val MAX_SIZE_BYTES = 1 * 1024 * 1024

    private val allowedMimeTypes = setOf(
        "image/jpeg",
        "image/jpg",
        "image/png",
        "image/webp"
    )

    fun validateImage(mimeType: String, sizeInBytes: Long): String? {
        if (sizeInBytes > MAX_SIZE_BYTES) {
            return "File size must be less than 1MB"
        }

        if (!allowedMimeTypes.contains(mimeType.lowercase())) {
            return "Invalid file type. Only JPEG, PNG, WebP are allowed"
        }

        return null // Valid
    }

    fun isImageFile(fileName: String): Boolean {
        val extension = fileName.substringAfterLast('.', "").lowercase()
        return extension in listOf("jpg", "jpeg", "png", "webp")
    }
}
