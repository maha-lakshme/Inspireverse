package com.maha.inspireverse.data

import com.maha.inspireverse.model.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesApi {
    @GET("quotes/random")
    suspend fun getRandomQuote(@Query("count") query:Int): Response<List<Quote>>
    @GET("quotes/random")
    suspend fun getQuotesByTag(@Query("tags") query:String):Response<List<Quote>>
}
//https://github.com/theriturajps/Quotes-API