package org.rajat.quickpick.utils

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private const val TAG = "CLOUDINARY_IMAGE_DEBUG"

actual class ImagePickerHelper(private val activity: ComponentActivity) {
    private var onImageSelectedCallback: ((ImageData) -> Unit)? = null
    private var onErrorCallback: ((String) -> Unit)? = null

    internal var launcherTrigger: (() -> Unit)? = null

    actual fun pickImage(onImageSelected: (ImageData) -> Unit, onError: (String) -> Unit) {
        Log.d(TAG, "pickImage: Called")
        onImageSelectedCallback = onImageSelected
        onErrorCallback = onError

        if (launcherTrigger != null) {
            Log.d(TAG, "pickImage: Launching image picker")
            launcherTrigger?.invoke()
        } else {
            Log.e(TAG, "pickImage: Launcher is not initialized")
            onError("Image picker is not ready. Please try again.")
        }
    }

    internal fun handleImageResult(uri: Uri?) {
        Log.d(TAG, "handleImageResult: uri=$uri")
        if (uri != null) {
            try {
                Log.d(TAG, "Processing selected image URI: $uri")
                val contentResolver = activity.contentResolver
                val inputStream = contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()
                Log.d(TAG, "Image bytes read: ${bytes?.size ?: 0} bytes")

                if (bytes != null) {
                    val mimeType = contentResolver.getType(uri) ?: "image/*"
                    val fileName = getFileName(uri) ?: "image.jpg"
                    val sizeInBytes = bytes.size.toLong()

                    Log.d(TAG, "Image data prepared - fileName=$fileName, mimeType=$mimeType, size=$sizeInBytes bytes")

                    val imageData = ImageData(
                        bytes = bytes,
                        fileName = fileName,
                        mimeType = mimeType,
                        sizeInBytes = sizeInBytes
                    )
                    Log.d(TAG, "Invoking onImageSelected callback")
                    onImageSelectedCallback?.invoke(imageData)
                } else {
                    Log.e(TAG, "Failed to read image data: bytes is null")
                    onErrorCallback?.invoke("Failed to read image data")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing image: ${e.message}", e)
                onErrorCallback?.invoke("Error processing image: ${e.message}")
            }
        } else {
            Log.w(TAG, "No image selected by user")
            onErrorCallback?.invoke("No image selected")
        }
    }

    private fun getFileName(uri: Uri): String? {
        Log.d(TAG, "getFileName: Attempting to get filename for URI: $uri")
        val cursor = activity.contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && it.moveToFirst()) {
                val fileName = it.getString(nameIndex)
                Log.d(TAG, "getFileName: Retrieved filename=$fileName")
                fileName
            } else {
                Log.w(TAG, "getFileName: Could not retrieve filename from URI")
                null
            }
        }
    }
}

@Composable
actual fun rememberImagePickerHelper(): ImagePickerHelper {
    Log.d(TAG, "rememberImagePickerHelper: Creating ImagePickerHelper")
    val context = LocalContext.current
    val activity = context as ComponentActivity

    val helper = remember(activity) {
        Log.d(TAG, "rememberImagePickerHelper: Initializing ImagePickerHelper for activity=$activity")
        ImagePickerHelper(activity)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        Log.d(TAG, "rememberLauncherForActivityResult: Result received")
        helper.handleImageResult(uri)
    }

    helper.launcherTrigger = {
        Log.d(TAG, "Launching PickVisualMedia")
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    return helper
}
