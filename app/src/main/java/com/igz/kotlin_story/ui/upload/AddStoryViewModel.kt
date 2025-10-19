package com.igz.kotlin_story.ui.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.data.local.SessionManager
import com.igz.kotlin_story.data.remote.model.BaseResponse
import com.igz.kotlin_story.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(
    private val repo: StoryRepository,
    private val session: SessionManager
) : ViewModel() {

    private val _uploadState = MutableLiveData<ResultState<BaseResponse>>()
    val uploadState: LiveData<ResultState<BaseResponse>> = _uploadState

    fun upload(file: File, description: String, lat: Double? = null, lon: Double? = null) {
        _uploadState.value = ResultState.Loading
        viewModelScope.launch {
            val token = session.tokenFlow.first().orEmpty()
            _uploadState.value = repo.uploadStory(token, file, description, lat, lon)
        }
    }
}
