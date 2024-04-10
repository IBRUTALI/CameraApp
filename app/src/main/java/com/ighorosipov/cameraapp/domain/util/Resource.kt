package com.ighorosipov.cameraapp.domain.util

sealed class Resource<T: Any>(
    val data: T? = null,
    val message: String? = null
) {

    class Success<T: Any>(data: T): Resource<T>(data)
    class Error<T: Any>(data: T? = null, message: String?): Resource<T>(data, message)
    class Loading<T : Any> : Resource<T>()

}