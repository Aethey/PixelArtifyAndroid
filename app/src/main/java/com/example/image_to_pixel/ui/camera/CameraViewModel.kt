package com.example.image_to_pixel.ui.camera

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.image_to_pixel.PixelApplication
import java.util.concurrent.Executors
import com.example.image_to_pixel.utils.PhotoSaverRepository
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CameraViewModel(
    application: Application,
    private val photoSaver: PhotoSaverRepository
) : AndroidViewModel(application) {

    private val context: Context
        get() = getApplication<PixelApplication>()
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    var uiState by mutableStateOf(CameraState())
        private set


    suspend fun getCameraProvider(): ProcessCameraProvider {
        return suspendCoroutine { continuation ->
            ProcessCameraProvider.getInstance(context).apply {
                addListener({ continuation.resume(get()) }, cameraExecutor)
            }
        }
    }

    fun takePicture() {
        viewModelScope.launch {
            uiState = uiState.copy(isTakingPicture = true)

            val savedFile = photoSaver.generatePhotoCacheFile()

            uiState.imageCapture.takePicture(
                ImageCapture.OutputFileOptions.Builder(savedFile).build(),
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        Log.i("TakePicture", "capture succeeded")

                        photoSaver.cacheCapturedPhoto(savedFile)
                        uiState = uiState.copy(imageFile = savedFile)
                    }

                    override fun onError(ex: ImageCaptureException) {
                        Log.e("TakePicture", "capture failed", ex)
                    }
                }
            )
        }
    }
}

class CameraViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PixelApplication
        return CameraViewModel(app, app.photoSaver) as T
    }
}
