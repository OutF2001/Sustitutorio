package com.valdeos.flavio.sustitutorio

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.valdeos.flavio.sustitutorio.databinding.ActivityMainBinding
import com.valdeos.flavio.sustitutorio.repository.NewsRepository
import com.valdeos.flavio.sustitutorio.repository.local.NewsDatabase
import com.valdeos.flavio.sustitutorio.repository.network.NewsApiService
import com.valdeos.flavio.sustitutorio.view.adapter.NewsAdapter
import com.valdeos.flavio.sustitutorio.viewmodel.NewsViewModel
import com.valdeos.flavio.sustitutorio.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRetrofitAndViewModel()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRetrofitAndViewModel() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newsApiService = retrofit.create(NewsApiService::class.java)
        val newsDao = NewsDatabase.getDatabase(this).newsDao()
        val newsRepository = NewsRepository(newsDao, newsApiService)

        val viewModelFactory = NewsViewModelFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(emptyList()) { news ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("NEWS_TITLE", news.title)
            intent.putExtra("NEWS_DESCRIPTION", news.description)
            intent.putExtra("NEWS_IMAGE_URL", news.imageUrl)
            intent.putExtra("NEWS_URL", news.url)
            startActivity(intent)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = newsAdapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is NewsViewModel.UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorTextView.visibility = View.GONE
                    }
                    is NewsViewModel.UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorTextView.visibility = View.GONE
                        newsAdapter.updateNews(state.news)
                    }
                    is NewsViewModel.UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.errorTextView.visibility = View.VISIBLE
                        binding.errorTextView.text = state.message
                    }
                }
            }
        }
    }
}