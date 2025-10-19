package com.igz.kotlin_story.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.igz.kotlin_story.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_NAME) ?: ""
        val desc = intent.getStringExtra(EXTRA_DESC) ?: ""
        val photo = intent.getStringExtra(EXTRA_PHOTO) ?: ""

        binding.tvDetailName.text = name
        binding.tvDetailDescription.text = desc
        Glide.with(binding.ivDetailPhoto).load(photo).into(binding.ivDetailPhoto)
    }

    companion object {
        private const val EXTRA_ID = "extra_id"
        private const val EXTRA_NAME = "extra_name"
        private const val EXTRA_DESC = "extra_desc"
        private const val EXTRA_PHOTO = "extra_photo"

        fun newIntent(context: Context, id: String, name: String, desc: String, photo: String) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_NAME, name)
                putExtra(EXTRA_DESC, desc)
                putExtra(EXTRA_PHOTO, photo)
            }
    }
}
