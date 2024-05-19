package com.example.image_to_pixel.ui.convert

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.image_to_pixel.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream

class PixelConvertViewModel : ViewModel() {

    var pixelImageBitmap = mutableStateOf<Bitmap?>(null)
        private set

    var pixelImageUri = mutableStateOf<Uri?>(null)
        private set

    var originImageUri = mutableStateOf<Uri?>(null)
        private set

    private val _imageType = listOf(
        listOf(10, 32, 0),
        listOf(10, 64, 0),
        listOf(10, 32, 1)
    )

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String?) {
        viewModelScope.launch {
            val tempTitle = "pixel_image_${System.currentTimeMillis()}"
            val galleryUri = FileUtils.saveBitmapToGallery(context, bitmap, tempTitle)
            galleryUri.let {
                pixelImageUri.value = it
            }
        }

    }
    fun convertBitmapToUri(context: Context,bitmap: Bitmap){
        viewModelScope.launch {
            val uri = FileUtils.convertBitmapToUri(context, bitmap, "sample_image.png")
            uri.let {
                pixelImageUri.value = it
            }
        }
    }

    fun clearAll(){
        pixelImageUri.value = null
        pixelImageBitmap.value = null
    }

    fun saveOriginUrl(capturedImageUri:Uri){
        originImageUri.value = capturedImageUri
    }


}