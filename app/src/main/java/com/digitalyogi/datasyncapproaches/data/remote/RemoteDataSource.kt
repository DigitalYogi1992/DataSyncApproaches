package com.digitalyogi.datasyncapproaches.data.remote

import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class FetchResult<out T> {
    data class Success<T>(val data: T) : FetchResult<T>()
    data class HttpError(val code: Int, val message: String) : FetchResult<Nothing>()
    data class NetworkError(val exception: Throwable) : FetchResult<Nothing>()
}

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun sendDataSafely(content: String): FetchResult<RemoteResponse> {
        return try {
            val response = apiService.submitData(RemoteRequest(content))
            // If "success" is false but still 200, you can treat it as an error or partial success
            if (response.success) {
                FetchResult.Success(response)
            } else {
                FetchResult.HttpError(400, response.message)
            }
        } catch (e: HttpException) {
            FetchResult.HttpError(e.code(), e.message())
        } catch (e: IOException) {
            FetchResult.NetworkError(e)
        } catch (e: Exception) {
            FetchResult.NetworkError(e)
        }
    }

    suspend fun fetchDataSafely(): FetchResult<List<RemoteRequest>> {
        return try {
            val list = apiService.getData()
            FetchResult.Success(list)
        } catch (e: HttpException) {
            FetchResult.HttpError(e.code(), e.message())
        } catch (e: IOException) {
            FetchResult.NetworkError(e)
        } catch (e: Exception) {
            FetchResult.NetworkError(e)
        }
    }
}
