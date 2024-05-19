package com.example.image_to_pixel

import androidx.compose.ui.graphics.Color
import com.example.PixelImage.R

object Constants {
    const val SHARE_IMAGE_TYPE = "image/jpeg"

    val colorShades = listOf(
        Color(0xFFFFFFFF), // 纯白色
        Color(0xFFF5F5F5), // 非常浅的灰色
        Color(0xFFEEEEEE), // 更浅的灰色
        Color(0xFFE0E0E0), // 浅灰色
        Color(0xFFBDBDBD), // 中等浅灰色
        Color(0xFF9E9E9E), // 中等灰色
        Color(0xFF757575), // 较深的灰色
        Color(0xFF616161)  // 深灰色
    )

    val imageResourceIds = listOf(
        R.drawable.example1,
        R.drawable.example2,
        R.drawable.example3
    )
    val contentList = listOf(
        "Example", "Pixel", "Images"
    )
    val radioButtonTexts = listOf("SMALL PIXEL", "LARGE PIXEL", "ISOLATE PIXEL")
    const val PERMISSION_REQUEST_TITLE1 = "Camera access"
    const val PERMISSION_REQUEST_TEXT1 = "Permissions should be called in the context of an Activity"
    const val PERMISSION_REQUEST_TEXT2 = "PixelConvert would like access to the camera to be able take picture when creating a log"
    const val IMAGE_UPLOAD_TITLE = "Update\nfrom⬇️"
    const val WIDGET_DESCRIPTION1 = "⬆️Here is a sample image."
    const val WIDGET_DESCRIPTION2 = "Switch options to browse image previews.⬇️"
    const val ERROR_TIMEOUT_MESSAGE = "timeout 😭"

}