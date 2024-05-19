package com.example.image_to_pixel

import android.app.Application
import com.example.image_to_pixel.utils.PermissionManager
import com.example.image_to_pixel.utils.PhotoSaverRepository

class PixelApplication : Application() {
    lateinit var photoSaver: PhotoSaverRepository
    lateinit var permissions: PermissionManager

    override fun onCreate() {
        super.onCreate()

        photoSaver = PhotoSaverRepository(this, this.contentResolver)
        permissions = PermissionManager(this)
    }
}