package com.valdeos.flavio.sustitutorio.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.valdeos.flavio.sustitutorio.databinding.ItemNewsBinding
import com.valdeos.flavio.sustitutorio.model.News

class NewsAdapter(
    private var news: List<News>,
    private val onItemClick: (News) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    fun updateNews(newsList: List<News>) {
        news = newsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(news[position])
    }

    override fun getItemCount() = news.size

    inner class NewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.apply {
                titleTextView.text = news.title
                descriptionTextView.text = news.description

                Glide.with(root.context)
                    .load(news.imageUrl)
                    .into(imageView)

                root.setOnClickListener { onItemClick(news) }
            }
        }
    }
}