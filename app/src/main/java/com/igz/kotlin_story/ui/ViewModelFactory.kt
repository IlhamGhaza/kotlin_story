package com.igz.kotlin_story.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.igz.kotlin_story.di.Injection
import com.igz.kotlin_story.ui.auth.AuthViewModel
import com.igz.kotlin_story.ui.home.StoryListViewModel
import com.igz.kotlin_story.ui.upload.AddStoryViewModel

class ViewModelFactory private constructor(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(Injection.provideAuthRepository(context)) as T
            }
            modelClass.isAssignableFrom(StoryListViewModel::class.java) -> {
                StoryListViewModel(Injection.provideStoryRepository(), Injection.provideSessionManager(context)) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(Injection.provideStoryRepository(), Injection.provideSessionManager(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(context.applicationContext).also { instance = it }
        }
    }
}
