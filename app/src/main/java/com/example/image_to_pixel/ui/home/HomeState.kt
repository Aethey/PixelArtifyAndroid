package com.example.image_to_pixel.ui.home

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

data class HomeState(
    val hasCameraAccess: Boolean,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val date: Long,
    val capturedImage: File? = null,
    val localPickerPhoto: Uri? = null,
    val pixelImageBitmap : Bitmap? = null,
    val pixelImageUri : Uri? = null,
    val originImageUri : Uri? = null,
    val savedPhoto: File? = null,
    val selectedConvertOption: Int = 0
)