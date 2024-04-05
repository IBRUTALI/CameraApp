package com.ighorosipov.cameraapp.presentation.ui.components.screens

import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ighorosipov.cameraapp.presentation.ui.components.CameraPreview

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    controller: LifecycleCameraController,
    onGalleryClick: () -> Unit,
    onTakePhoto: () -> Unit,
    onRecordClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        CameraPreview(
            controller = controller,
            modifier = Modifier.fillMaxSize()
        )
        SmallIcon(
            imageVector = Icons.Default.Cameraswitch,
            modifier = Modifier.offset(16.dp, 16.dp)
        ) {
            controller.cameraSelector =
                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            SmallIcon(imageVector = Icons.Default.Photo) { onGalleryClick() }
            SmallIcon(imageVector = Icons.Default.PhotoCamera) { onTakePhoto() }
            SmallIcon(imageVector = Icons.Default.Videocam) { onRecordClick() }
        }
    }
}

@Composable
fun SmallIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onIconClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = { onIconClick() }
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null
        )
    }
}