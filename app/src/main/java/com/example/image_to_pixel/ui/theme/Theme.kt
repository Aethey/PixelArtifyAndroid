package com.example.image_to_pixel.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import com.example.image_to_pixel.Constants
import kotlin.random.Random

private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    secondary = Color.White,
//    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color.Black,
//    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

// custom pixel BG
fun DrawScope.drawPixelMatrix(pixelSize: Float, rows: Int, columns: Int) {
    for (row in 0 until rows) {
        for (column in 0 until columns) {
            val color = Constants.colorShades[Random.nextInt(Constants.colorShades.size)]
            drawRect(
                color = color,
                topLeft = androidx.compose.ui.geometry.Offset(column * pixelSize, row * pixelSize),
                size = androidx.compose.ui.geometry.Size(pixelSize, pixelSize)
            )
        }
    }
}

@Composable
fun PixelImageTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val textScheme = when{
        darkTheme -> pixelFontFamilyDark
        else -> pixelFontFamilyLight
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = textScheme,
        content = content
    )
}