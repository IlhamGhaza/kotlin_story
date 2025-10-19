package com.igz.kotlin_story.data.repository

import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.data.local.SessionManager
import com.igz.kotlin_story.data.remote.ApiService
import com.igz.kotlin_story.data.remote.model.LoginResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(
    private val api: ApiService,
    private val session: SessionManager
) {
    suspend fun login(email: String, password: String): ResultState<LoginResult> = withContext(Dispatchers.IO) {
        try {
            val res = api.login(email, password)
            if (res.isSuccessful) {
                val body = res.body()
                if (body != null && body.error == false && body.loginResult != null) {
                    session.saveLogin(
                        token = body.loginResult.token,
                        userId = body.loginResult.userId,
                        name = body.loginResult.name
                    )
                    return@withContext ResultState.Success(body.loginResult)
                }
                return@withContext ResultState.Error(body?.message ?: "Unknown error")
            } else {
                return@withContext ResultState.Error("${res.code()} ${res.message()}", res.code())
            }
        } catch (e: Exception) {
            return@withContext ResultState.Error(e.message ?: "Exception")
        }
    }

    suspend fun register(name: String, email: String, password: String): ResultState<Unit> = withContext(Dispatchers.IO) {
        try {
            val res = api.register(name, email, password)
            if (res.isSuccessful) {
                val body = res.body()
                if (body != null && body.error == false) {
                    return@withContext ResultState.Success(Unit)
                }
                return@withContext ResultState.Error(body?.message ?: "Unknown error")
            } else {
                return@withContext ResultState.Error("${res.code()} ${res.message()}", res.code())
            }
        } catch (e: Exception) {
            return@withContext ResultState.Error(e.message ?: "Exception")
        }
    }

    suspend fun logout() {
        session.logout()
    }
}
