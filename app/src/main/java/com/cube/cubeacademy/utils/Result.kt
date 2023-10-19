package com.cube.cubeacademy.utils

import java.lang.Exception

//Sealed class for representing different API states
sealed interface ApiResult{
    data object Loading: ApiResult
    data class Error(val error: String?): ApiResult
    data class Success<T>(val data: T): ApiResult
}
