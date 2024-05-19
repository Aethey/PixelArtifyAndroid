package com.example.image_to_pixel.ui.convert

import android.net.Uri

data class PixelConvertState (
    val id: String, // 照片的唯一标识符
    val uriString: String, // 照片的 URI
    val uri: Uri,// 照片的 URI
    val filePath: String, // 照片的文件路径
    val dateTaken: Long, // 拍摄日期，使用时间戳表示
    val name: String? = null, // 照片的名称，默认为空
    val description: String? = null, // 照片的描述，默认为空
    val location: String? = null //
)