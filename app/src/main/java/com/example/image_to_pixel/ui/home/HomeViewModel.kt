package com.example.image_to_pixel.ui.home

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.image_to_pixel.PixelApplication
import com.example.image_to_pixel.utils.FileUtils
import com.example.image_to_pixel.utils.MediaRepository
import com.example.image_to_pixel.utils.PhotoSaverRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Calendar

class HomeViewModel (
    application: Application,
    private val photoSaver: PhotoSaverRepository
) : AndroidViewModel(application){
    private val context: Context
        get() = getApplication()

    private val mediaRepository = MediaRepository(context)

    var uiState by mutableStateOf(
        HomeState(
            hasCameraAccess = hasPermission(Manifest.permission.CAMERA),
            date = getTodayDateInMillis(),
        )
    )
        private set

    private val _imageType = listOf(
        listOf(10, 32, 0),
        listOf(10, 64, 0),
        listOf(10, 32, 1)
    )

    fun isValid(): Boolean {
        return !photoSaver.isEmpty() && !uiState.isSaving
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun onPermissionChange(permission: String, isGranted: Boolean) {
        when (permission) {
            Manifest.permission.CAMERA -> {
                uiState = uiState.copy(hasCameraAccess = isGranted)
            }
            else -> {
                Log.e("Permission change", "Unexpected permission: $permission")
            }
        }
    }

    private fun getTodayDateInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        return calendar.timeInMillis
    }

    fun onDateChange(dateInMillis: Long) {
        uiState = uiState.copy(date = dateInMillis)
    }


    fun loadLocalPickerPictures() {
        viewModelScope.launch {
            mediaRepository.fetchSingleImage()?.let { entity ->
                uiState = uiState.copy(localPickerPhoto = entity.uri)
            }
        }
    }

    fun onSaveFileToGallery(){
        viewModelScope.launch {
            uiState.savedPhoto?.let { photoSaver.saveFileToGallery(context, it) }
        }
    }

    fun onLocalPhotoPickerSelect(photo: Uri) {
        viewModelScope.launch {
            photoSaver.cacheFromUri(photo)
            refreshSavedPhoto()
        }
    }

    fun onPhotoPickerSelect(photo: Uri?) {
        viewModelScope.launch {
            if (photo != null) {
                photoSaver.cacheFromUris(photo)
            }
            refreshSavedPhoto()
        }
    }

    // region Photo management


    fun refreshSavedPhoto() {
        println("refreshSavedPhoto save photo!!!")
        uiState = uiState.copy(savedPhoto = photoSaver.getPhotos())
    }

    fun onPhotoRemoved(photo: File) {
        viewModelScope.launch {
            photoSaver.removeFile(photo)
            refreshSavedPhoto()
        }
    }

    fun selectedConvertOption(type:Int){
        uiState = uiState.copy(selectedConvertOption = type)
    }

    fun convertImage(file: File?,imageType: Int) {
        println("imageType.value.toString() is :")
        println(imageType.toString())
        viewModelScope.launch {
            Toast.makeText(context, "Loading...", Toast.LENGTH_SHORT).show()
            file?.let {
                val path =
                    withContext(Dispatchers.IO) { FileUtils.copyUriToInternalStorage(file) }
                path?.let { inputPath ->
                    val resultPath = withContext(Dispatchers.IO) {
                        FileUtils.convertImage(
                            inputPath,
                            inputPath,
                            kernelSize = _imageType[imageType][0],
                            pixelSize = _imageType[imageType][1],
                            withPadding = _imageType[imageType][2]
                        )
                    }
                    val bitmap =
                        withContext(Dispatchers.IO) { FileUtils.loadImageFromFile(resultPath) }
                    uiState = uiState.copy(pixelImageBitmap = bitmap)
                }
            }
        }
    }
}

class HomeViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val app = extras[APPLICATION_KEY] as PixelApplication
        return HomeViewModel(app, app.photoSaver) as T
    }
}