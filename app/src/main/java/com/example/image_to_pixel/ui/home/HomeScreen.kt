package com.example.image_to_pixel.ui.home

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.PixelImage.R
import com.example.image_to_pixel.Constants
import com.example.image_to_pixel.Screens
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random


@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory()),
) {
// parma
    val state = viewModel.uiState
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
        viewModel::onPhotoPickerSelect
    )
    val requestCameraPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.onPermissionChange(CAMERA, isGranted)
                navController.navigate(Screens.Camera.route)
            } else {
                coroutineScope.launch {
                    snackBarHostState.showSnackbar("Camera currently disabled due to denied permission.")
                }
            }
        }
    var showExplanationDialogForCameraPermission by remember { mutableStateOf(false) }
    if (showExplanationDialogForCameraPermission) {
        CameraExplanationDialog(
            onConfirm = {
                requestCameraPermission.launch(CAMERA)
                showExplanationDialogForCameraPermission = false
            },
            onDismiss = { showExplanationDialogForCameraPermission = false },
        )
    }
// function
    LaunchedEffect(Unit) {
        viewModel.refreshSavedPhoto()
    }

    fun onSelectImageFromGallery() {
        coroutineScope.launch {
            pickImage.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    }

    fun onTakePhoto() {
        when {
            state.hasCameraAccess -> navController.navigate(Screens.Camera.route)
            ActivityCompat.shouldShowRequestPermissionRationale(
                context.getActivity(),
                CAMERA
            ) -> showExplanationDialogForCameraPermission = true

            else -> requestCameraPermission.launch(CAMERA)
        }
    }

    fun onConvertImage() {
        viewModel.convertImage(state.selectImageFile, state.selectedConvertOption)
    }
    fun onShareImage() {
        state.pixelImageFile?.let { shareImage(context, it) }
    }
    fun onSaveImage(){
        viewModel.onSaveFileToGallery()
    }

    fun onOptionSelected(index: Int) {
        viewModel.selectedConvertOption(index)
    }

    fun onClearUIState(){
        viewModel.onClearUIState()
    }

// init widget
    HomeScreenContent(
        context,
        state.selectImageFile,
        state.pixelImageBitmap,
        onSelectImageFromGallery = { onSelectImageFromGallery() },
        onConvertImage = {
            onConvertImage()
        },
        onSaveImage = { onSaveImage() },
        onShareImage = { onShareImage()
        },
        onOptionSelected = {
            onOptionSelected(it)
        },
        onClear = {
            onClearUIState()
        },
        takePhotoAction = {
            onTakePhoto()
        },
        selectedOption = state.selectedConvertOption,
    )
}


@Composable
fun HomeScreenContent(
    context: Context,
    capturedImageUri: File?,
    pixelImageBitmap: Bitmap?,
    onSelectImageFromGallery: () -> Unit,
    onConvertImage: () -> Unit,
    onSaveImage: () -> Unit,
    onShareImage: () -> Unit,
    onClear: () -> Unit,
    takePhotoAction: () -> Unit,
    onOptionSelected: (Int) -> Unit,
    selectedOption: Int,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LocalConfiguration.current.screenWidthDp.dp
    val pixelFontFamily = remember {
        FontFamily(
            typeface = ResourcesCompat.getFont(
                context,
                R.font.joystix_monospace
            )!!
        )
    }

    Box(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .background(color = Color.White), contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            DisplayImage(pixelImageBitmap, screenHeight, pixelFontFamily, selectedOption)
            ActionButtons(pixelImageBitmap, pixelFontFamily, onSaveImage, onShareImage, onClear)
            DisplayCapturedImage(
                capturedImageUri,
                pixelFontFamily,
                screenWidth,
                selectedOption,
                onOptionSelected
            )
            BottomBar(onSelectImageFromGallery, onConvertImage, takePhotoAction)

        }
    }
}


@Composable
fun DisplayImage(
    pixelImageBitmap: Bitmap?,
    screenHeight: Dp,
    pixelFontFamily: FontFamily,
    selectedOption: Int,
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeight - 360.dp),
        contentAlignment = Alignment.Center
    ) {
        pixelImageBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier
                    .height(screenHeight - 360.dp)
                    .fillMaxWidth()
                    .padding(4.dp), contentScale = ContentScale.Fit
            )
        } ?: run {
            // place widget
            DisplayImagePlaceHolder(screenHeight, pixelFontFamily, selectedOption)
        }
    }
}

@Composable
private fun DisplayImagePlaceHolder(
    screenHeight: Dp,
    pixelFontFamily: FontFamily,
    selectedOption: Int
) {
    Box(
        modifier = Modifier
            .height(screenHeight - 420.dp)
            .fillMaxWidth()
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        val contentList = listOf(
            "Example", "Pixel", "Images"
        )

        @Composable
        fun CustomText(index: Int) {
            Text(
                contentList[index],
                color = Color.Black,
                style = TextStyle(fontFamily = pixelFontFamily),
                fontSize = 36.sp
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            CustomText(0)
            CustomText(1)
            CustomText(2)
            Image(
                painter = painterResource(id = Constants.imageResourceIds[selectedOption]),
                contentDescription = "Icon",
            )
        }
    }
}

@Composable
fun ActionButtons(
    pixelImageBitmap: Bitmap?,
    pixelFontFamily: FontFamily,
    onSaveImage: () -> Unit,
    onShareImage: () -> Unit,
    onClear: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
    ) {
        pixelImageBitmap?.let {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomButton({
                    onSaveImage()
                }, R.drawable.common_save, null)
                CustomButton({
                    onShareImage()
                }, R.drawable.common_share, null)
                CustomButton({
                    onClear()
                }, R.drawable.common_trash, null)
            }

        } ?: run {
            ActionButtons(pixelFontFamily)
        }
    }
}

@Composable
private fun ActionButtons(pixelFontFamily: FontFamily) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "⬆️Here is a sample image.",
            color = Color.Black,
            style = TextStyle(fontFamily = pixelFontFamily),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "Switch options to browse image previews.⬇️",
            color = Color.Black,
            style = TextStyle(fontFamily = pixelFontFamily),
            modifier = Modifier.weight(1f)
        )

    }

//
}

@Composable
fun DisplayCapturedImage(
    capturedImage: File?,
    pixelFontFamily: FontFamily,
    screenWidth: Dp,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        capturedImage?.let {
            AsyncImage(
                modifier = Modifier.size(160.dp),
                model = capturedImage,
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.example0),
                        modifier = Modifier.width(120.dp),
                        contentDescription = "Icon",
                    )
                    Text(
                        "Update\nfrom⬇️",
                        color = Color.Black,
                        style = TextStyle(fontFamily = pixelFontFamily, fontSize = 16.sp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                val options = listOf(0, 1, 2)
                CustomRadioGroup(
                    options = options,
                    optionsShow = Constants.radioButtonTexts,
                    selectedOption = selectedOption,
                    onOptionSelected = onOptionSelected,
                    pixelFontFamily = pixelFontFamily,
                    screenWidth = screenWidth
                )

            }

        }
    }
}

@Composable
fun BottomBar(
    onSelectImageFromGallery: () -> Unit,
    onConvertImage: () -> Unit,
    onCameraPermissionRequest: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomButton({ onCameraPermissionRequest() }, R.drawable.common_camera, null)
            CustomButton({ onSelectImageFromGallery() }, R.drawable.common_gallery, null)
            CustomButton(
                { onConvertImage() },
                R.drawable.common_convert,
                null
            )
        }
    }
}


@Composable
fun CustomButton(onCustomClick: () -> Unit, imageID: Int, modifier: Modifier?) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        onClick = {
            onCustomClick()
        },
        modifier = modifier ?: Modifier
    ) {
        Image(
            painter = painterResource(id = imageID),
            contentDescription = "Icon",
            modifier = Modifier.size(48.dp)
        )
    }
}

fun shareImage(context: Context, file: File) {
    val contentUri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, contentUri)
        type = "image/jpeg"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, null))
}

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
fun CustomRadioButton(
    selected: Boolean,
    selectedIcon: Int,
    contentDescription: String,
) {
    if (selected)
        Image(
            painter = painterResource(id = selectedIcon),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    else
        Box(modifier = Modifier.size(24.dp)) {}
}


@Composable
fun CustomRadioGroup(
    options: List<Int>,
    optionsShow: List<String>,
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    pixelFontFamily: FontFamily,
    screenWidth: Dp,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        options.forEach { index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .noRippleClickable { onOptionSelected(index) }
            ) {
                CustomRadioButton(
                    selected = selectedOption == index,
                    selectedIcon = R.drawable.pixel_arrow,
                    contentDescription = optionsShow[index]
                )
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    modifier = Modifier
                        .width(screenWidth / 2)
                        .background(Color.White)
                        .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
                        .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    val parts = optionsShow[index].split(" ")
                    for (part in parts) {
                        Text(
                            text = part,
                            style = TextStyle(
                                fontFamily = pixelFontFamily,
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CameraExplanationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Camera access") },
        text = { Text("PhotoLog would like access to the camera to be able take picture when creating a log") },
        icon = {
            Icon(
                Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surfaceTint
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Continue")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Context.getActivity(): Activity {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}