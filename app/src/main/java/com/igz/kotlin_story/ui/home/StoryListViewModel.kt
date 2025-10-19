package com.igz.kotlin_story.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.data.local.SessionManager
import com.igz.kotlin_story.data.remote.model.Story
import com.igz.kotlin_story.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StoryListViewModel(
    private val repo: StoryRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _stories = MutableLiveData<ResultState<List<Story>>>()
    val stories: LiveData<ResultState<List<Story>>> = _stories

    fun load() {
        _stories.value = ResultState.Loading
        viewModelScope.launch {
            val token = session.tokenFlow.first().orEmpty()
            _stories.value = repo.getStories(token, page = 1, size = 30, location = 0)
        }
    }
}
