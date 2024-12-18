package com.valdeos.flavio.sustitutorio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valdeos.flavio.sustitutorio.model.News
import com.valdeos.flavio.sustitutorio.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _news = MutableStateFlow<List<News>>(emptyList())
    val news: StateFlow<List<News>> = _news

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchNews()
    }

    fun fetchNews() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val fetchedNews = repository.fetchNews()
                _news.value = fetchedNews
                _uiState.value = if (fetchedNews.isNotEmpty()) {
                    UiState.Success(fetchedNews)
                } else {
                    UiState.Error("No se encontraron noticias")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    sealed class UiState {
        object Loading : UiState()
        data class Success(val news: List<News>) : UiState()
        data class Error(val message: String) : UiState()
    }
}