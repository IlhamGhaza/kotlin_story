package com.igz.kotlin_story.data.remote

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import com.igz.kotlin_story.data.remote.model.BaseResponse
import com.igz.kotlin_story.data.remote.model.LoginResponse
import com.igz.kotlin_story.data.remote.model.StoriesResponse
import com.igz.kotlin_story.data.remote.model.StoryDetailResponse

private const val BASE_URL = "https://story-api.dicoding.dev/v1/"

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<BaseResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") bearer: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): Response<StoriesResponse>

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") bearer: String,
        @Path("id") id: String
    ): Response<StoryDetailResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") bearer: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: String,
        @Part("lat") lat: Double? = null,
        @Part("lon") lon: Double? = null
    ): Response<BaseResponse>

    companion object {
        fun create(): ApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}
