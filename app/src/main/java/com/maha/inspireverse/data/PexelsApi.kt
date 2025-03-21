package com.maha.inspireverse.data

import com.maha.inspireverse.model.PexelsImage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApi {
    @GET("/v1/search")
    suspend fun searchImages(
        @Query("query") query: String,
        @Header("Authorization") apiKey: String
    ): Response<PexelsImage>
}
//"z54ItlsgEc2BsYo8g6VeKZssWeoTcEpk8KMoVEiA5SDMqzKOcWZIUGs6"