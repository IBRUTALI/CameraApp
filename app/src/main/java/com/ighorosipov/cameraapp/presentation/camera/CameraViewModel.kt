package com.ighorosipov.cameraapp.presentation.camera

import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.ViewModel
import com.ighorosipov.cameraapp.domain.repository.CameraService
import com.ighorosipov.cameraapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val cameraService: CameraService
): ViewModel() {

    private val _photoCaptureState = MutableStateFlow<Resource<String>>(Resource.Loading())
    val photoCaptureState = _photoCaptureState.asStateFlow()

    private val _videoCaptureState = MutableStateFlow<Resource<String>>(Resource.Loading())
    val videoCaptureState = _videoCaptureState.asStateFlow()

    fun onTakePhoto() {
        _photoCaptureState.value = Resource.Loading()
        _photoCaptureState.value = cameraService.takePhoto()
    }

    fun recordVideo() {
        _videoCaptureState.value = Resource.Loading()
        _videoCaptureState.value = cameraService.recordVideo()
    }

    fun changeCameraMode() {
        cameraService.changeCameraMode()
    }

    fun getController(): LifecycleCameraController {
        return cameraService.getController()
    }

}