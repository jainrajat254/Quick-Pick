package org.rajat.quickpick.utils

sealed class ImageUploadState {
    object Idle : ImageUploadState()
    object Uploading : ImageUploadState()
    data class Success(val imageUrl: String) : ImageUploadState()
    data class Error(val message: String) : ImageUploadState()
}

