package com.maha.inspireverse.repository

import com.maha.inspireverse.data.PexelsApi
import com.maha.inspireverse.data.QuotesApi
import com.maha.inspireverse.model.Photos
import com.maha.inspireverse.model.Quote

interface QuoteRepository {
    suspend fun fetchQuotes(limit:Int):List<Quote>
    suspend fun fetchImages(category:String):List<Photos>

}
class RetrofitQuoteRepository(private val quotesApi: QuotesApi,private val pexelsApi: PexelsApi):QuoteRepository{
    override suspend fun fetchQuotes(limit: Int): List<Quote> {
        val response = quotesApi.getRandomQuote(limit)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }

    override suspend fun fetchImages(category: String): List<Photos> {
        val response = pexelsApi.searchImages(category, "z54ItlsgEc2BsYo8g6VeKZssWeoTcEpk8KMoVEiA5SDMqzKOcWZIUGs6")
        return if (response.isSuccessful) response.body()?.photos ?: emptyList() else emptyList()
    }

}