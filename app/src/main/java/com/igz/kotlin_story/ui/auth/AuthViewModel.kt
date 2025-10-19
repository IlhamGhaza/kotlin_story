package com.igz.kotlin_story.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.data.remote.model.LoginResult
import com.igz.kotlin_story.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {
    private val _loginState = MutableLiveData<ResultState<LoginResult>>()
    val loginState: LiveData<ResultState<LoginResult>> = _loginState

    private val _registerState = MutableLiveData<ResultState<Unit>>()
    val registerState: LiveData<ResultState<Unit>> = _registerState

    fun login(email: String, password: String) {
        _loginState.value = ResultState.Loading
        viewModelScope.launch {
            _loginState.value = repo.login(email, password)
        }
    }

    fun register(name: String, email: String, password: String) {
        _registerState.value = ResultState.Loading
        viewModelScope.launch {
            _registerState.value = repo.register(name, email, password)
        }
    }
}
