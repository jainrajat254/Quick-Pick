package org.rajat.quickpick.utils

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.darwin.NSObject
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTTypeImage
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalForeignApi::class)
actual class ImagePickerHelper(private val viewController: UIViewController) {

    private var onImageSelectedCallback: ((ImageData) -> Unit)? = null
    private var onErrorCallback: ((String) -> Unit)? = null

    actual fun pickImage(onImageSelected: (ImageData) -> Unit, onError: (String) -> Unit) {
        this.onImageSelectedCallback = onImageSelected
        this.onErrorCallback = onError

        val picker = UIImagePickerController()
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary
        picker.delegate = ImagePickerDelegate(
            onImagePicked = { image ->
                processImage(image)
            },
            onError = { errorMessage ->
                onError(errorMessage)
            }
        )

        viewController.presentViewController(picker, animated = true, completion = null)
    }

    @OptIn(ExperimentalTime::class)
    private fun processImage(image: UIImage) {
        try {
            val imageData = UIImageJPEGRepresentation(image, 0.8)
                ?: throw Exception("Failed to convert image to JPEG")

            val bytes = imageData.toByteArray()

            val fileName = "image_${Clock.System.now().toEpochMilliseconds()}.jpg"
            val mimeType = "image/jpeg"
            val sizeInBytes = bytes.size.toLong()

            val imageDataObject = ImageData(
                bytes = bytes,
                fileName = fileName,
                mimeType = mimeType,
                sizeInBytes = sizeInBytes
            )

            onImageSelectedCallback?.invoke(imageDataObject)
        } catch (e: Exception) {
            onErrorCallback?.invoke(e.message ?: "Failed to process image")
        }
    }

    private fun NSData.toByteArray(): ByteArray {
        return ByteArray(this.length.toInt()).apply {
            usePinned {
                platform.posix.memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private class ImagePickerDelegate(
    private val onImagePicked: (UIImage) -> Unit,
    private val onError: (String) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo["UIImagePickerControllerOriginalImage"] as? UIImage

        picker.dismissViewControllerAnimated(true, completion = null)

        if (image != null) {
            onImagePicked(image)
        } else {
            onError("Failed to get image")
        }
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
        onError("Image selection cancelled")
    }
}

@Composable
actual fun rememberImagePickerHelper(): ImagePickerHelper {
    val rootViewController = platform.UIKit.UIApplication.sharedApplication.keyWindow?.rootViewController
        ?: throw IllegalStateException("No root view controller found")

    return androidx.compose.runtime.remember {
        ImagePickerHelper(rootViewController)
    }
}
