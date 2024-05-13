package com.ighorosipov.cameraapp.domain.repository

import androidx.camera.view.LifecycleCameraController
import com.ighorosipov.cameraapp.domain.util.Resource

interface CameraService {

    fun recordVideo(): Resource<String>

    fun takePhoto(): Resource<String>

    fun changeCameraMode()

    fun getController(): LifecycleCameraController

}