package com.example.image_to_pixel.ui.home

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

data class HomeState(
    val hasCameraAccess: Boolean,
    val date: Long,
    val pixelImageBitmap : Bitmap? = null,
    val pixelImageFile : File? = null,
    val selectImageFile: File? = null,
    val selectedConvertOption: Int = 0,
    var processing: LoadingState = LoadingState.COMPLETE,
    var errorMessage: String? = null

)

enum class LoadingState {
    LOADING, TIMEOUT, COMPLETE,ERROR
}