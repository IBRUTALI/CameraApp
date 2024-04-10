package com.ighorosipov.cameraapp.di

import android.content.Context
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Provides
    fun provideCameraController(context: Context): LifecycleCameraController {
        return LifecycleCameraController(context)
    }

}