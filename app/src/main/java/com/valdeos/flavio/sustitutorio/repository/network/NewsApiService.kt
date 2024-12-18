package com.valdeos.flavio.sustitutorio.repository.network

import com.valdeos.flavio.sustitutorio.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = "8711301dd46f4447ac9ae4310c627fe0"
    ): Response<NewsResponse>
}