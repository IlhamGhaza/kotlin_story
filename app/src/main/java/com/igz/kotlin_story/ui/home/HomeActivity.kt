package com.igz.kotlin_story.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.igz.kotlin_story.R
import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.data.local.SessionManager
import com.igz.kotlin_story.data.remote.model.Story
import com.igz.kotlin_story.databinding.ActivityHomeBinding
import com.igz.kotlin_story.ui.ViewModelFactory
import com.igz.kotlin_story.ui.auth.LoginActivity
import com.igz.kotlin_story.ui.detail.DetailActivity
import com.igz.kotlin_story.ui.upload.AddStoryActivity
import com.igz.kotlin_story.ui.settings.SettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: StoryListViewModel
    private val adapter = StoryAdapter { view, item -> onStoryClicked(view, item) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(this))[StoryListViewModel::class.java]

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = adapter

        binding.swipe.setOnRefreshListener { viewModel.load() }

        binding.fabAdd.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        viewModel.stories.observe(this) { state ->
            binding.progress.isVisible = state is ResultState.Loading
            binding.swipe.isRefreshing = false
            when (state) {
                is ResultState.Loading -> {
                    binding.tvEmpty.visibility = View.GONE
                }
                is ResultState.Success -> {
                    val list = state.data
                    adapter.submitList(list)
                    binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                }
                is ResultState.Error -> {
                    binding.tvEmpty.visibility = View.VISIBLE
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.load()
    }

    override fun onResume() {
        super.onResume()
        // Refresh list when returning from AddStoryActivity
        viewModel.load()
    }

    private fun onStoryClicked(sharedView: View, item: Story) {
        val intent = DetailActivity.newIntent(this, item.id, item.name, item.description, item.photoUrl)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair(sharedView, "photo_${'$'}{item.id}")
        )
        startActivity(intent, options.toBundle())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Clear session and finish task
                CoroutineScope(Dispatchers.IO).launch {
                    SessionManager(this@HomeActivity).logout()
                }
                val intent = LoginActivity.newIntent(this)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                true
            }
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}
