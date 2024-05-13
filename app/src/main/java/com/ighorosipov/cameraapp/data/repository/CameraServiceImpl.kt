package com.ighorosipov.cameraapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import com.ighorosipov.cameraapp.MainActivity
import com.ighorosipov.cameraapp.domain.repository.CameraService
import com.ighorosipov.cameraapp.domain.util.Resource
import java.io.File
import javax.inject.Inject

class CameraServiceImpl @Inject constructor(
    private val context: Context
): CameraService {

    private var recording: Recording? = null

    private val controller =
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.VIDEO_CAPTURE
            )
        }

    @SuppressLint("MissingPermission")
    override fun recordVideo(): Resource<String> {
        var result: Resource<String> = Resource.Success(data = "Video capture succeeded")

        if(recording != null) {
            recording?.stop()
            recording = null
            return Resource.Success(data = "Video capture succeeded")
        }

        if(!hasRequiredPermissions()) {
            return Resource.Error(message = "No permissions")
        }

        val outputFile = File(context.filesDir, "Video_${System.currentTimeMillis()}.mp4")

        recording = controller.startRecording(
            FileOutputOptions.Builder(outputFile).build(),
            AudioConfig.create(true),
            ContextCompat.getMainExecutor(context)
        ) { event ->
            when(event) {
                is VideoRecordEvent.Finalize -> {
                    if(event.hasError()) {
                        recording?.close()
                        recording = null
                        result = Resource.Error(message = "Video capture failed")
                    }
                }
            }
        }
        return result
    }

    override fun takePhoto(): Resource<String> {

        var result: Resource<String> = Resource.Success(data = "Image saved")

        if(!hasRequiredPermissions()) {
            return Resource.Error(message = "No permissions")
        }

        val outputFile = File(context.filesDir, "Image_${System.currentTimeMillis()}.jpg")

        controller.takePicture(
            OutputFileOptions.Builder(outputFile).build(),
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    result = Resource.Success(data = "Image saved")
                }

                override fun onError(exception: ImageCaptureException) {
                    result = Resource.Error(message = "Image captured failed")
                }

            }
        )

        return result
    }

    override fun changeCameraMode() {
        controller.cameraSelector =
            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }
    }

    override fun getController(): LifecycleCameraController {
        return controller
    }

    private fun hasRequiredPermissions(): Boolean {
        return MainActivity.CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

}