package com.example.image_to_pixel

import androidx.compose.ui.graphics.Color
import com.example.PixelImage.R

object Constants {
    val radioButtonTexts = listOf("SMALL PIXEL", "LARGE PIXEL", "ISOLATE PIXEL")

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

}