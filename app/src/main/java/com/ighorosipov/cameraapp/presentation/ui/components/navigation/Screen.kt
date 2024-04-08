package com.ighorosipov.cameraapp.presentation.ui.components.navigation

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main_screen")
    data object CameraScreen : Screen("camera_screen")
    data object ProfileScreen : Screen("profile_screen")
}