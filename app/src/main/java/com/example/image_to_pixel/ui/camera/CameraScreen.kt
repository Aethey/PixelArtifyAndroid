package com.example.image_to_pixel.ui.camera

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.PixelImage.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    navController: NavHostController,
    viewModel: CameraViewModel = viewModel(factory = CameraViewModelFactory())
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val state = viewModel.uiState

    val previewUseCase = remember { Preview.Builder().build() }

    LaunchedEffect(Unit) {
        val cameraProvider = viewModel.getCameraProvider()
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                previewUseCase,
                state.imageCapture
            )
        } catch (ex: Exception) {
            Log.e("CameraCapture", "Failed to bind camera use cases", ex)
        }
    }

    LaunchedEffect(state.imageFile) {
        if (state.imageFile != null) {
            navController.popBackStack()
        }
    }

    Scaffold(
        floatingActionButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                onClick = {
                    if (!state.isTakingPicture) viewModel.takePicture()
                },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.common_camera),
                    contentDescription = "Icon",
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    ) { innerPadding ->
        AndroidView(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

                    previewUseCase.setSurfaceProvider(this.surfaceProvider)
                }
            }
        )
    }
}

