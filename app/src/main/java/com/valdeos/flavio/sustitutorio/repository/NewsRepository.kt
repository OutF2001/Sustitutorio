package com.valdeos.flavio.sustitutorio.repository

import com.valdeos.flavio.sustitutorio.model.News
import com.valdeos.flavio.sustitutorio.repository.local.NewsDao
import com.valdeos.flavio.sustitutorio.repository.network.NewsApiService
import kotlinx.coroutines.flow.Flow

class NewsRepository(
    private val newsDao: NewsDao,
    private val newsApiService: NewsApiService
) {
    suspend fun fetchNews(): List<News> {
        return try {
            val response = newsApiService.getTopHeadlines()
            if (response.isSuccessful) {
                val news = response.body()?.articles ?: emptyList()
                newsDao.deleteAll()
                newsDao.insertAll(news)
                news
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getLocalNews(): Flow<List<News>> = newsDao.getAllNews()
}