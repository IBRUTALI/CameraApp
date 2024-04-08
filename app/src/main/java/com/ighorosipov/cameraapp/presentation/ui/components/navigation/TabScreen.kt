package com.ighorosipov.cameraapp.presentation.ui.components.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun TabScreen(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = {  },
        bottomBar = { MainBottomNavigation(navController = navController) }
    ) { paddingValues ->
        TabNavGraph(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}