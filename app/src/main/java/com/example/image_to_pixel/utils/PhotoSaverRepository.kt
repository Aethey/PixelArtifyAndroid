/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.image_to_pixel.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

class PhotoSaverRepository(context: Context, private val contentResolver: ContentResolver) {

    private var _imageFile:File? = null

    fun getImageFile() = _imageFile
    fun isEmpty() = _imageFile == null

    private val cacheFolder = File(context.cacheDir, "pixelImage").also { it.mkdir() }
    private val pixelImageFolder = File(context.filesDir, "pixelImage").also { it.mkdir() }

    private fun generateFileName() = "${System.currentTimeMillis()}.png"
    private fun generatePhotoLogFile() = File(pixelImageFolder, generateFileName())
    fun generatePhotoCacheFile() = File(cacheFolder, generateFileName())

    fun cacheCapturedPhoto(imageFile: File) {
        _imageFile = imageFile
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun cacheFromUri(uri: Uri) {
        withContext(Dispatchers.IO) {
            contentResolver.openInputStream(uri)?.use { input ->
                val cachedPhoto = generatePhotoCacheFile()

                cachedPhoto.outputStream().use { output ->
                    input.copyTo(output)
                    _imageFile = cachedPhoto
                }
            }
        }
    }

    suspend fun cacheFromUris(uri: Uri) {
        cacheFromUri(uri)
    }

    suspend fun removeFile(imageFile: File) {
        withContext(Dispatchers.IO) {
            imageFile.delete()
            _imageFile = null
        }
    }

    suspend fun savePhotos(): File {
        return withContext(Dispatchers.IO) {
            val savedPhotos = generatePhotoLogFile()
            _imageFile = null
            savedPhotos
        }
    }

    suspend fun saveFileToGallery(context: Context, file: File) {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, generateFileName())
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            val inputStream = FileInputStream(file)
            outputStream?.use { stream ->
                inputStream.use { input ->
                    input.copyTo(stream)
                }
            }
        }
    }
}