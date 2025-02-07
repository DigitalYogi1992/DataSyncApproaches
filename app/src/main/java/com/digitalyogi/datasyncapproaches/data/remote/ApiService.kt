package com.digitalyogi.datasyncapproaches.data.remote


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class RemoteRequest(val content: String)
data class RemoteResponse(val success: Boolean, val message: String)

interface ApiService {

    @POST("submitData")
    suspend fun submitData(@Body request: RemoteRequest): RemoteResponse

    @GET("getData")
    suspend fun getData(): List<RemoteRequest>
}
