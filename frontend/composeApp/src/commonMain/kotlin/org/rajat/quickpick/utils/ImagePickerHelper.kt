package org.rajat.quickpick.utils

import androidx.compose.runtime.Composable

data class ImageData(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String,
    val sizeInBytes: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ImageData

        if (!bytes.contentEquals(other.bytes)) return false
        if (fileName != other.fileName) return false
        if (mimeType != other.mimeType) return false
        if (sizeInBytes != other.sizeInBytes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + sizeInBytes.hashCode()
        return result
    }
}

expect class ImagePickerHelper {
    fun pickImage(onImageSelected: (ImageData) -> Unit, onError: (String) -> Unit)
}

@Composable
expect fun rememberImagePickerHelper(): ImagePickerHelper
