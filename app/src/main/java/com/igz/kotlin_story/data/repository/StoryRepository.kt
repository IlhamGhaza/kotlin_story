package com.igz.kotlin_story.data.repository

import com.igz.kotlin_story.core.ResultState
import com.igz.kotlin_story.data.remote.ApiService
import com.igz.kotlin_story.data.remote.model.BaseResponse
import com.igz.kotlin_story.data.remote.model.StoriesResponse
import com.igz.kotlin_story.data.remote.model.Story
import com.igz.kotlin_story.data.remote.model.StoryDetailResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class StoryRepository(private val api: ApiService) {

    suspend fun getStories(token: String, page: Int? = null, size: Int? = null, location: Int? = null): ResultState<List<Story>> =
        withContext(Dispatchers.IO) {
            try {
                val res = api.getStories("Bearer $token", page, size, location)
                if (res.isSuccessful) {
                    val body: StoriesResponse? = res.body()
                    if (body != null && !body.error) {
                        return@withContext ResultState.Success(body.listStory)
                    }
                    return@withContext ResultState.Error(body?.message ?: "Unknown error")
                } else {
                    return@withContext ResultState.Error("${res.code()} ${res.message()}", res.code())
                }
            } catch (e: Exception) {
                return@withContext ResultState.Error(e.message ?: "Exception")
            }
        }

    suspend fun getStoryDetail(token: String, id: String): ResultState<Story> = withContext(Dispatchers.IO) {
        try {
            val res = api.getStoryDetail("Bearer $token", id)
            if (res.isSuccessful) {
                val body: StoryDetailResponse? = res.body()
                if (body != null && !body.error) {
                    return@withContext ResultState.Success(body.story)
                }
                return@withContext ResultState.Error(body?.message ?: "Unknown error")
            } else {
                return@withContext ResultState.Error("${res.code()} ${res.message()}", res.code())
            }
        } catch (e: Exception) {
            return@withContext ResultState.Error(e.message ?: "Exception")
        }
    }

    suspend fun uploadStory(token: String, imageFile: File, description: String, lat: Double? = null, lon: Double? = null): ResultState<BaseResponse> =
        withContext(Dispatchers.IO) {
            try {
                val reqImage: RequestBody = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("photo", imageFile.name, reqImage)
                val res = api.uploadStory("Bearer $token", part, description, lat, lon)
                if (res.isSuccessful) {
                    val body = res.body()
                    if (body != null && !body.error) {
                        return@withContext ResultState.Success(body)
                    }
                    return@withContext ResultState.Error(body?.message ?: "Unknown error")
                } else {
                    return@withContext ResultState.Error("${res.code()} ${res.message()}", res.code())
                }
            } catch (e: Exception) {
                return@withContext ResultState.Error(e.message ?: "Exception")
            }
        }
}
