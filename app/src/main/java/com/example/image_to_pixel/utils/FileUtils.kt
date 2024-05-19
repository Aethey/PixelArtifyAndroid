package com.example.image_to_pixel.utils


import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.chaquo.python.Python
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


object FileUtils {

    private fun copyAssetFileToInternalStorage(
        context: Context,
        assetManager: AssetManager,
        fileName: String
    ): String {
        val file = File(context.filesDir, fileName)
        assetManager.open(fileName).use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file.absolutePath
    }

    fun convertImage(
        imagePath: String,
        filePath: String,
        kernelSize: Int,
        pixelSize: Int,
        withPadding: Int
    ): String {
        val py = Python.getInstance()
        val pyf = py.getModule("convert")
        return pyf.callAttr("convert", imagePath, filePath, kernelSize, pixelSize, withPadding)
            .toString()
    }


    fun convertAssetsImage(
        context: Context,
        assetManager: AssetManager,
        imageName: String,
        filePath: String,
        kernelSize: Int,
        pixelSize: Int,
        edgeThresh: Int,
    ): String {
        val fileDescriptor = copyAssetFileToInternalStorage(context, assetManager, imageName)
        val py = Python.getInstance()
        val pyf = py.getModule("convert")
        return pyf.callAttr("convert", fileDescriptor, filePath, kernelSize, pixelSize, edgeThresh)
            .toString()
    }


    fun loadImageFromFile(filePath: String): Bitmap? {
        val file = File(filePath)
        if (file.exists()) {
            return BitmapFactory.decodeFile(filePath)
        }
        return null
    }

    fun generateImageFilePath(context: Context, fileName: String): String {
        val file = File(context.filesDir, fileName)
        return file.absolutePath
    }

    // 将 URI 转换为文件路径
    fun copyUriToInternalStorage(file: File): String? {

        try {
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var fileName: String? = null
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
        return fileName
    }

    fun convertBitmapToUri(context: Context, bitmap: Bitmap, filename: String): Uri? {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, filename)
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String): Uri? {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, title)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
        val uri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(it)
            outputStream?.use { stream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            }
        }
        return uri
    }

    fun saveFileToGallery(context: Context, file: File, title: String) {
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, title)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png") // 请根据文件类型调整 MIME 类型
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