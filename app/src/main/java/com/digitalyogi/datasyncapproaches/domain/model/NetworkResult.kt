package com.digitalyogi.datasyncapproaches.domain.model

/**
 * Sealed class to represent network call outcomes
 * in a way that doesn't crash on HttpException (e.g. 403).
 */
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class HttpError(val code: Int, val message: String) : NetworkResult<Nothing>()
    data class NetworkError(val error: Throwable) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

