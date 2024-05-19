package com.example.image_to_pixel

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.example.image_to_pixel.ui.camera.CameraScreen
import com.example.image_to_pixel.ui.home.HomeScreen
import com.example.image_to_pixel.ui.theme.PixelImageTheme
import com.example.image_to_pixel.utils.PermissionManager
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var permissionManager: PermissionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
        permissionManager = (application as PixelApplication).permissions

        enableEdgeToEdge()
        setContent {
            PixelImageTheme {
                App()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            permissionManager.checkPermissions()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    val startNavigation = Screens.Home.route
    NavHost(navController = navController, startDestination = startNavigation) {
        composable(Screens.Home.route) { HomeScreen(navController) }
        composable(Screens.Camera.route) { CameraScreen(navController) }
    }
}

sealed class Screens(val route: String) {
    data object Home : Screens("home")
    data object Camera : Screens("camera")
}
