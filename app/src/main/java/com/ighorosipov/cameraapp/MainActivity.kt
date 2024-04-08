package com.ighorosipov.cameraapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.ighorosipov.cameraapp.presentation.camera.CameraViewModel
import com.ighorosipov.cameraapp.presentation.ui.components.navigation.RootNavigationGraph
import com.ighorosipov.cameraapp.presentation.ui.theme.CameraAppTheme
import java.io.File

class MainActivity : ComponentActivity() {

    private var recording: Recording? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                CAMERAX_PERMISSIONS,
                0
            )
        }
        setContent {
            CameraAppTheme {
                RootNavigationGraph(navController = rememberNavController())
//                BottomSheetScaffold(
//                    scaffoldState = scaffoldState,
//                    sheetPeekHeight = 0.dp,
//                    sheetContent = {
//                        PhotoBottomSheetContent(
//                            bitmaps = bitmaps,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                ) { paddingValues ->
//                    CameraScreen(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(paddingValues),
//                        controller = controller,
//                        onGalleryClick = {
//                            scope.launch {
//                                scaffoldState.bottomSheetState.expand()
//                            }
//                        },
//                        onTakePhoto = {
//                            takePhoto(
//                                controller = controller,
//                                onPhotoTaken = viewModel::onTakePhoto
//                            )
//                        },
//                        onRecordClick = {
//                            recordVideo(controller)
//                        }
//                    )
//                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun recordVideo(controller: LifecycleCameraController) {

        if(recording != null) {
            recording?.stop()
            recording = null
            return
        }

        if(!hasRequiredPermissions()) {
            return
        }
        val outputFile = File(filesDir, "my-recording.mp4")

        recording = controller.startRecording(
            FileOutputOptions.Builder(outputFile).build(),
            AudioConfig.create(true),
            ContextCompat.getMainExecutor(applicationContext)
        ) { event ->
            when(event) {
                is VideoRecordEvent.Finalize -> {
                    if(event.hasError()) {
                        recording?.close()
                        recording = null

                        Toast.makeText(applicationContext, "Video capture failed", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Video capture succeeded", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun takePhoto(
        controller: LifecycleCameraController,
        onPhotoTaken: (Bitmap) -> Unit,
    ) {

        if(!hasRequiredPermissions()) {
            return
        }

        controller.takePicture(
            ContextCompat.getMainExecutor(applicationContext),
            object : OnImageCapturedCallback() {

                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val matrix = Matrix().apply {
                        postRotate(image.imageInfo.rotationDegrees.toFloat())
                        if (controller.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                            postScale(-1f, 1f)
                        }
                    }

                    val rotatedBitmap = Bitmap.createBitmap(
                        image.toBitmap(),
                        0,
                        0,
                        image.width,
                        image.height,
                        matrix,
                        true
                    )

                    onPhotoTaken(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                }

            }
        )
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }

}