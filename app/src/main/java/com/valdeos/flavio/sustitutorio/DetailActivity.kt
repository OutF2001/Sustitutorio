package com.valdeos.flavio.sustitutorio

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.valdeos.flavio.sustitutorio.databinding.ActivityDetailBinding
import com.google.android.material.appbar.MaterialToolbar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: MaterialToolbar = binding.topAppBar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (savedInstanceState != null) {
            // Restaurar datos si la actividad fue recreada
            binding.titleTextView.text = savedInstanceState.getString("NEWS_TITLE", "")
            binding.descriptionTextView.text = savedInstanceState.getString("NEWS_DESCRIPTION", "")
            val imageUrl = savedInstanceState.getString("NEWS_IMAGE_URL", "")
            Glide.with(this).load(imageUrl).into(binding.imageView)
        } else {
            // Si es la primera vez que se crea la actividad, recuperar datos del Intent
            val title = intent.getStringExtra("NEWS_TITLE") ?: ""
            val description = intent.getStringExtra("NEWS_DESCRIPTION") ?: ""
            val imageUrl = intent.getStringExtra("NEWS_IMAGE_URL") ?: ""
            val url = intent.getStringExtra("NEWS_URL") ?: ""

            binding.titleTextView.text = title
            binding.descriptionTextView.text = description

            Glide.with(this)
                .load(imageUrl)
                .into(binding.imageView)

            binding.sourceButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }

            binding.shareButton.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Mira esta noticia: $url")
                }
                startActivity(Intent.createChooser(shareIntent, "Compartir Noticia"))
            }
        }
    }

    // Guardar el estado cuando la actividad se va a destruir
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("NEWS_TITLE", binding.titleTextView.text.toString())
        outState.putString("NEWS_DESCRIPTION", binding.descriptionTextView.text.toString())
        outState.putString("NEWS_IMAGE_URL", intent.getStringExtra("NEWS_IMAGE_URL"))
        outState.putString("NEWS_URL", intent.getStringExtra("NEWS_URL"))
    }
}
