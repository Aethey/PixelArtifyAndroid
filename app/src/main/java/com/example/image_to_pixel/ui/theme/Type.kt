package com.example.image_to_pixel.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.example.PixelImage.R

// Set of Material typography styles to start with

val customFont = FontFamily(
    Font(resId = R.font.joystix_monospace)
)

val pixelFontFamilyLight = Typography(
    bodyLarge = TextStyle(
        fontFamily = customFont,
        fontSize = 36.sp,
        color = Color.Black
    ),
    bodyMedium = TextStyle(
        fontFamily = customFont,
        fontSize = 14.sp,
        color = Color.Black
    ),
    bodySmall = TextStyle(
        fontFamily = customFont,
        color = Color.Black,
        fontSize = 12.sp
    ),
)

val pixelFontFamilyDark = Typography(
    bodyLarge = TextStyle(
        fontFamily = customFont,
        fontSize = 36.sp,
        color = Color.White
    ),
    bodyMedium = TextStyle(
        fontFamily = customFont,
        fontSize = 14.sp,
        color = Color.White
    ),
    bodySmall = TextStyle(
        fontFamily = customFont,
        color = Color.White,
        fontSize = 12.sp
    ),
)





val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)