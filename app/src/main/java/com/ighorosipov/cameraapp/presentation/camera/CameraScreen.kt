package com.ighorosipov.cameraapp.presentation.camera

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.ighorosipov.cameraapp.presentation.ui.components.CameraPreview
import com.ighorosipov.cameraapp.presentation.ui.components.PhotoBottomSheetContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val viewModel = hiltViewModel<CameraViewModel>()

    val controller = remember {
        viewModel.getController()
    }

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
                viewModel.changeCameraMode()
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
                    viewModel.onTakePhoto()
                }
                SmallIcon(imageVector = Icons.Default.Videocam) {
                    viewModel.recordVideo()
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