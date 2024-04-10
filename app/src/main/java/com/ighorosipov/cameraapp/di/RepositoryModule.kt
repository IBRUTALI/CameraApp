package com.ighorosipov.cameraapp.di

import com.ighorosipov.cameraapp.data.repository.CameraServiceImpl
import com.ighorosipov.cameraapp.domain.repository.CameraService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Singleton
    @Binds
    fun bindCameraService(cameraServiceImpl: CameraServiceImpl): CameraService

}