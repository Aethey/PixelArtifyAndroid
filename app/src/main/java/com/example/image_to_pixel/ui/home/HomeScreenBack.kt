//package com.example.image_to_pixel.ui.home
//
//import android.Manifest
//import android.content.Context
//import android.content.Intent
//import android.graphics.Bitmap
//import android.net.Uri
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.safeDrawingPadding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.MutableState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableIntStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.graphics.drawscope.DrawScope
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.content.res.ResourcesCompat
//import androidx.navigation.NavController
//import coil.compose.rememberAsyncImagePainter
//import com.example.PixelImage.R
//import com.example.image_to_pixel.Constants
//import com.example.image_to_pixel.ui.convert.PixelConvertViewModel
//import com.google.accompanist.permissions.ExperimentalPermissionsApi
//import com.google.accompanist.permissions.PermissionState
//import com.google.accompanist.permissions.isGranted
//import com.google.accompanist.permissions.rememberPermissionState
//import kotlin.random.Random
//
//
//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun HomeScreenBack(
//    pixelConvertViewModel: PixelConvertViewModel,
//    navController: NavController,
//    context: Context
//) {
//
////    var capturedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
//    var capturedImageUri by pixelConvertViewModel.originImageUri
//    println("startcapturedImageUri")
//    println(capturedImageUri)
//
//    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
//    val pixelImageBitmap by pixelConvertViewModel.pixelImageBitmap
//    println(capturedImageUri)
//    val pixelImageUri by pixelConvertViewModel.pixelImageUri
//    val launcher = rememberLauncherForActivityResult(
//        contract =
//        ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        capturedImageUri = uri
//        if (uri != null) {
//            pixelConvertViewModel.saveOriginUrl(uri)
//        }
//    }
//    val selectedOption = remember { mutableIntStateOf(0) }
//    LaunchedEffect(navController.currentBackStackEntry) {
//        navController.currentBackStackEntry?.savedStateHandle?.get<Uri>("capturedImageUri")?.let {
//            println("capturedImageUri is $it")
////            capturedImageUri = it
//            pixelConvertViewModel.saveOriginUrl(it)
//        }
//    }
//
//    HomeScreenContent(
//        context,
//        capturedImageUri,
//        pixelImageBitmap,
//        onImageCapture = { launcher.launch("image/*") },
//        onConvertImage = { imageUri ->
//            pixelConvertViewModel.convertImage(context, imageUri, selectedOption)
//        },
//        onSaveImage = { bitmap ->
//            pixelConvertViewModel.saveBitmapToGallery(context, bitmap,null)
//        },
//        onShareImage = {bitmap ->
//            pixelConvertViewModel.convertBitmapToUri(context, bitmap)
//            pixelImageUri?.let { shareImage(context, it) }
//        },
//        onClear = {
//            capturedImageUri = null
//            pixelConvertViewModel.clearAll()
//        },
//        takePhotoAction = {
//            takePhoto(
//                navController,
//                cameraPermissionState,
//                context
//            )
//        },
//        selectedOption = selectedOption,
//    )
//}
//
//
//@Composable
//fun HomeScreenContent(
//    context: Context,
//    capturedImageUri: Uri?,
//    pixelImageBitmap: Bitmap?,
//    onImageCapture: () -> Unit,
//    onConvertImage: (Uri?) -> Unit,
//    onSaveImage: (Bitmap) -> Unit,
//    onShareImage:(Bitmap) -> Unit,
//    onClear:() -> Unit,
//    takePhotoAction: () -> Unit,
//    selectedOption: MutableState<Int>,
//) {
//    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
//    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
//    LocalConfiguration.current.screenWidthDp.dp
//    val pixelFontFamily = remember {
//        FontFamily(
//            typeface = ResourcesCompat.getFont(
//                context,
//                R.font.joystix_monospace
//            )!!
//        )
//    }
//
//    Box(
//        modifier = Modifier
//            .safeDrawingPadding()
//            .fillMaxSize()
//            .background(color = Color.White), contentAlignment = Alignment.Center
//    ) {
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(bottom = 8.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceAround
//        ) {
//            DisplayImage(pixelImageBitmap, screenHeight, pixelFontFamily,selectedOption)
//            ActionButtons(pixelImageBitmap,pixelFontFamily,onSaveImage,onShareImage,onClear)
//            DisplayCapturedImage(capturedImageUri, pixelFontFamily, screenWidth, selectedOption)
//            BottomBar(capturedImageUri, onImageCapture, onConvertImage, takePhotoAction)
//
//        }
//    }
//}
//
//
//@Composable
//fun DisplayImage(pixelImageBitmap: Bitmap?, screenHeight: Dp, pixelFontFamily: FontFamily,selectedOption: MutableState<Int>,) {
//    val imageResourceIds = listOf(
//        R.drawable.example1,
//        R.drawable.example2,
//        R.drawable.example3
//    )
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(screenHeight - 360.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        pixelImageBitmap?.let {
//            Image(
//                bitmap = it.asImageBitmap(), contentDescription = null, modifier = Modifier
//                    .height(screenHeight - 360.dp)
//                    .fillMaxWidth()
//                    .padding(4.dp), contentScale = ContentScale.Fit
//            )
//        } ?: run {
//            // place widget
//            Box(
//                modifier = Modifier
//                    .height(screenHeight - 420.dp)
//                    .fillMaxWidth()
//                    .padding(4.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxHeight(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.SpaceAround
//                ) {
//                    Text(
//                        "Example",
//                        color = Color.Black,
//                        style = TextStyle(fontFamily = pixelFontFamily),
//                        fontSize = 36.sp
//
//                    )
//                    Text(
//                        "Pixel",
//                        color = Color.Black,
//                        style = TextStyle(fontFamily = pixelFontFamily),
//                        fontSize = 36.sp
//
//                    )
//                    Text(
//                        "Images",
//                        color = Color.Black,
//                        style = TextStyle(fontFamily = pixelFontFamily),
//                        fontSize = 36.sp
//
//                    )
//                    Image(
//                        painter = painterResource(id = imageResourceIds[selectedOption.value]),
//                        contentDescription = "Icon",
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ActionButtons(pixelImageBitmap: Bitmap?,
//                  pixelFontFamily: FontFamily,
//                  onSaveImage: (Bitmap) -> Unit,
//                  onShareImage:(Bitmap) -> Unit,
//                  onClear:() -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(80.dp)
//            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp)
//    ) {
//        pixelImageBitmap?.let {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp)
//                    .background(Color.White, RoundedCornerShape(12.dp))
//                    .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
//                    .padding(4.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                CustomButton({
//                    onSaveImage(pixelImageBitmap)
//                }, R.drawable.common_save, null)
//                CustomButton({
//                    onShareImage(pixelImageBitmap)
//                }, R.drawable.common_share, null)
//                CustomButton({
//                    onClear()
//                }, R.drawable.common_trash, null)
//            }
//
//        } ?: run {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(80.dp)
//                    .background(Color.White, RoundedCornerShape(12.dp))
//                    .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceAround,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "⬆️Here is a sample image.",
//                    color = Color.Black,
//                    style = TextStyle(fontFamily = pixelFontFamily),
//                    modifier = Modifier.weight(1f)
//                )
//                Text(
//                    text = "Switch options to browse image previews.⬇️",
//                    color = Color.Black,
//                    style = TextStyle(fontFamily = pixelFontFamily),
//                            modifier = Modifier.weight(1f)
//                )
//
//            }
//
//        }
//
//
//    }
//}
//
//@Composable
//fun DisplayCapturedImage(
//    capturedImageUri: Uri?,
//    pixelFontFamily: FontFamily,
//    screenWidth: Dp,
//    selectedOption: MutableState<Int>,
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(160.dp)
//            .padding(start = 16.dp, end = 16.dp),
//        horizontalArrangement = Arrangement.Start,
//    ) {
//        capturedImageUri?.let {
//            Image(
//                painter = rememberAsyncImagePainter(capturedImageUri),
//                contentDescription = null,
//                modifier = Modifier.size(160.dp),
//                contentScale = ContentScale.Fit
//            )
//        } ?: run {
//            Box(
//                modifier = Modifier
//                    .size(160.dp),
//                contentAlignment = Alignment.Center
//            ) {
//               Column(
//                   modifier = Modifier.fillMaxSize(),
//                   verticalArrangement = Arrangement.Center,
//                   horizontalAlignment = Alignment.CenterHorizontally
//               ) {
//
//                   Image(
//                       painter = painterResource(id = R.drawable.example0),
//                       modifier = Modifier.width(120.dp),
//                       contentDescription = "Icon",
//                   )
//                   Text(
//                       "Update\nfrom⬇️",
//                       color = Color.Black,
//                       style = TextStyle(fontFamily = pixelFontFamily,fontSize = 16.sp)
//                   )
//               }
//            }
//        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(160.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(160.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.SpaceAround
//            ) {
//                val options = listOf(0, 1, 2)
//                CustomRadioGroup(
//                    options = options,
//                    optionsShow = Constants.radioButtonTexts,
//                    selectedOption = selectedOption,
//                    onOptionSelected = { selectedOption.value = it },
//                    pixelFontFamily = pixelFontFamily,
//                    screenWidth = screenWidth
//                )
//
//            }
//
//        }
//    }
//}
//
//@Composable
//fun BottomBar(
//    capturedImageUri: Uri?,
//    onImageCapture: () -> Unit,
//    onConvertImage: (Uri?) -> Unit,
//    onCameraPermissionRequest: () -> Unit
//) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(80.dp)
//            .padding(start = 16.dp, end = 16.dp, top = 4.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(80.dp)
//                .background(Color.White, RoundedCornerShape(12.dp))
//                .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
//                .padding(4.dp),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            CustomButton({ onCameraPermissionRequest() }, R.drawable.common_camera, null)
//            CustomButton({ onImageCapture() }, R.drawable.common_gallery, null)
//            CustomButton(
//                { onConvertImage(capturedImageUri) },
//                R.drawable.common_convert,
//                null
//            )
//        }
//    }
//}
//
//
//@Composable
//fun CustomButton(onCustomClick: () -> Unit, imageID: Int, modifier: Modifier?) {
//    Button(
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.Transparent
//        ),
//        onClick = {
//            onCustomClick()
//        },
//        modifier = modifier ?: Modifier
//    ) {
//        Image(
//            painter = painterResource(id = imageID),
//            contentDescription = "Icon",
//            modifier = Modifier.size(48.dp)
//        )
//    }
//}
//
//@OptIn(ExperimentalPermissionsApi::class)
//fun takePhoto(
//    navController: NavController,
//    cameraPermissionState: PermissionState,
//    context: Context
//) {
//    cameraPermissionState.launchPermissionRequest().apply {
//        if (cameraPermissionState.status.isGranted) {
//            navController.navigate("camera")
//        } else {
//            Toast.makeText(context, "Need camera permission", Toast.LENGTH_SHORT).show()
//        }
//    }
//}
//
//fun shareImage(context: Context,contentUri:Uri){
//    val shareIntent: Intent = Intent().apply {
//        action = Intent.ACTION_SEND
//        putExtra(Intent.EXTRA_STREAM, contentUri)
//        type = "image/jpeg"
//    }
//    context.startActivity(Intent.createChooser(shareIntent, null))
//}
//
//// custom BG
//fun DrawScope.drawPixelMatrix(pixelSize: Float, rows: Int, columns: Int) {
//    for (row in 0 until rows) {
//        for (column in 0 until columns) {
//            val color = Constants.colorShades[Random.nextInt(Constants.colorShades.size)]
//            drawRect(
//                color = color,
//                topLeft = androidx.compose.ui.geometry.Offset(column * pixelSize, row * pixelSize),
//                size = androidx.compose.ui.geometry.Size(pixelSize, pixelSize)
//            )
//        }
//    }
//}
//
//@Composable
//fun CustomRadioButton(
//    selected: Boolean,
//    selectedIcon: Int,
//    unselectedIcon: Int?,
//    contentDescription: String,
//) {
//    if (selected)
//        Image(
//            painter = painterResource(id = selectedIcon),
//            contentDescription = contentDescription,
//            modifier = Modifier.size(24.dp)
//        )
//    else
//        Box(modifier = Modifier.size(24.dp)) {}
//}
//
//
//@Composable
//fun CustomRadioGroup(
//    options: List<Int>,
//    optionsShow: List<String>,
//    selectedOption: MutableState<Int>,
//    onOptionSelected: (Int) -> Unit,
//    pixelFontFamily: FontFamily,
//    screenWidth: Dp,
//) {
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.SpaceAround
//    ) {
//        options.forEach { index ->
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .noRippleClickable { onOptionSelected(index) }
//            ) {
//                CustomRadioButton(
//                    selected = selectedOption.value == index,
//                    selectedIcon = R.drawable.pixel_arrow,
//                    unselectedIcon = null,
//                    contentDescription = optionsShow[index]
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Row(
//                    modifier = Modifier
//                        .width(screenWidth / 2)
//                        .background(Color.White)
//                        .border(2.dp, Color.Black, RoundedCornerShape(6.dp))
//                        .padding(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                ) {
//                    val parts = optionsShow[index].split(" ")
//                    for (part in parts) {
//                        Text(
//                            text = part,
//                            style = TextStyle(
//                                fontFamily = pixelFontFamily,
//                                color = Color.Black,
//                                fontSize = 14.sp
//                            )
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
//    this.clickable(
//        indication = null,
//        interactionSource = remember { MutableInteractionSource() }) {
//        onClick()
//    }
//}
