package com.ighorosipov.cameraapp.presentation.camera

import android.widget.Toast
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ighorosipov.cameraapp.data.repository.CameraServiceImpl
import com.ighorosipov.cameraapp.domain.util.Resource
import com.ighorosipov.cameraapp.presentation.ui.components.CameraPreview
import com.ighorosipov.cameraapp.presentation.ui.components.PhotoBottomSheetContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    onGalleryClick: () -> Unit,
    onTakePhoto: () -> Unit,
    onRecordClick: () -> Unit
) {
    val cameraService = CameraServiceImpl(LocalContext.current)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val controller = remember {
        cameraService.controller
    }
    val context = LocalContext.current
    val viewModel = viewModel<CameraViewModel>()
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            PhotoBottomSheetContent(
                bitmaps = emptyList(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier.padding(paddingValues)
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
                SmallIcon(imageVector = Icons.Default.Photo) {
                    scope.launch {
                        scaffoldState.bottomSheetState.expand()
                    }
                }
                SmallIcon(imageVector = Icons.Default.PhotoCamera) {
                    cameraService.takePhoto().apply {
                        when(this) {
                            is Resource.Success -> Toast.makeText(context, this.data, Toast.LENGTH_SHORT).show()
                            is Resource.Error -> Toast.makeText(context, this.message, Toast.LENGTH_SHORT).show()
                            is Resource.Loading -> {}
                        }
                    }
                }
                SmallIcon(imageVector = Icons.Default.Videocam) {
                    cameraService.recordVideo()
                }
            }
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