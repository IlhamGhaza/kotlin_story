package com.igz.kotlin_story.di

import android.content.Context
import com.igz.kotlin_story.data.local.SessionManager
import com.igz.kotlin_story.data.remote.ApiService
import com.igz.kotlin_story.data.repository.AuthRepository
import com.igz.kotlin_story.data.repository.StoryRepository

object Injection {
    fun provideApiService(): ApiService = ApiService.create()

    fun provideSessionManager(context: Context): SessionManager = SessionManager(context)

    fun provideAuthRepository(context: Context): AuthRepository =
        AuthRepository(provideApiService(), provideSessionManager(context))

    fun provideStoryRepository(): StoryRepository = StoryRepository(provideApiService())
}
