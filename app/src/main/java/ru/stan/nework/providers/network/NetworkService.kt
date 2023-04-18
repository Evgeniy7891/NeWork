package ru.stan.nework.providers.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.stan.nework.domain.models.network.post.MediaResponse
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.post.MediaModel
import ru.stan.nework.domain.models.ui.post.Post

interface NetworkService {

    @GET("api/posts")
    suspend fun getPosts() : List<PostModel>

    @GET("api/posts/latest")
    suspend fun getLatest(@Query("count") count: Int) : Response<List<Post>>

    @GET("api/posts/{id}/before")
    suspend fun getBefore(
        @Path("id") id: Long,
        @Query("count") count: Int,
    ): Response<List<Post>>

    @GET("api/posts/{id}/after")
    suspend fun getAfter(
        @Path("id") id: Long,
        @Query("count") count: Int,
    ): Response<List<Post>>

    @FormUrlEncoded
    @POST("api/users/registration")
    suspend fun register(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("name") name: String
    ): AuthState

    @FormUrlEncoded
    @POST("api/users/authentication")
    suspend fun authentication(
        @Field("login") login: String,
        @Field("password") pass: String,
    ): AuthState

    @POST("api/posts")
    suspend fun addPost(@Body post: PostRequest): PostModel

    @Multipart
    @POST("api/media")
    suspend fun addMultimedia(@Part file: MultipartBody.Part?): MediaResponse

    @GET("api/users")
    suspend fun getUsers(): List<User>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Long): User

    @DELETE("api/posts/{id}")
    suspend fun removeById(@Path("id") id: Long)

    @GET("api/posts/{post_id}")
    suspend fun getPostById(@Path("post_id") id: Long): PostModel

    @POST("api/posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): PostModel

    @DELETE("api/posts/{id}/likes")
    suspend fun deleteLike(@Path("id") id: Long): PostModel
}