package com.ighorosipov.cameraapp.presentation.ui.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ighorosipov.cameraapp.presentation.camera.CameraScreen
import com.ighorosipov.cameraapp.presentation.main.MainScreen
import com.ighorosipov.cameraapp.presentation.profile.ProfileScreen

@Composable
fun TabNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = Graph.TAB,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            MainScreen(modifier = modifier)
        }
        composable(route = Screen.CameraScreen.route) {
            CameraScreen(
                modifier = modifier
            )
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(modifier = modifier)
        }
    }
}