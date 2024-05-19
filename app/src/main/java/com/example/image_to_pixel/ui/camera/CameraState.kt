package com.example.image_to_pixel.ui.camera

import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import java.io.File

data class CameraState(
    val isTakingPicture: Boolean = false,
    val imageCapture: ImageCapture = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build(),
    val imageFile: File? = null,
    val captureError: ImageCaptureException? = null,
    var capturedImageUri: Uri? = null
)
