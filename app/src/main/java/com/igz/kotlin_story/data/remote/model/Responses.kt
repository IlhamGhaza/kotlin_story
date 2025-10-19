package com.igz.kotlin_story.data.remote.model

import com.google.gson.annotations.SerializedName

// Base simple response
data class BaseResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String
)

// Login

data class LoginResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("loginResult") val loginResult: LoginResult?
)

data class LoginResult(
    @SerializedName("userId") val userId: String,
    @SerializedName("name") val name: String,
    @SerializedName("token") val token: String
)

// Stories list

data class StoriesResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("listStory") val listStory: List<Story>
)

data class Story(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("photoUrl") val photoUrl: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?
)

// Detail

data class StoryDetailResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("story") val story: Story
)
