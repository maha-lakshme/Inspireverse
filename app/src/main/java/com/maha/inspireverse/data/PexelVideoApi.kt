package com.maha.inspireverse.data

import com.maha.inspireverse.model.PexelsVideo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface PexelVideoApi {
    @GET("videos/popular")
    suspend fun getVideosFromApi(@Header("Authorization") apiKey:String):Response<PexelsVideo>
}