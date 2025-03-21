package com.maha.inspireverse.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitInstance {
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val quotesApi: QuotesApi by lazy {
        Retrofit.Builder().client(okHttpClient)
            .baseUrl("https://quoteslate.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesApi::class.java)
    }

    val pexelsApi: PexelsApi by lazy {
        Retrofit.Builder().client(okHttpClient)
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PexelsApi::class.java)
    }
    val pexelVideoApi :PexelVideoApi by lazy{
        Retrofit.Builder().client(okHttpClient)
            .baseUrl("https://api.pexels.com/v1/").
                addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(PexelVideoApi::class.java)
    }
}
