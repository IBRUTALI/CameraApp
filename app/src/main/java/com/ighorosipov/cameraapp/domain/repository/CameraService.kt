package com.ighorosipov.cameraapp.domain.repository

import com.ighorosipov.cameraapp.domain.util.Resource

interface CameraService {

    fun recordVideo(): Resource<String>

    fun takePhoto(): Resource<String>

}