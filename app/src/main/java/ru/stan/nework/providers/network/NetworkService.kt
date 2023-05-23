package ru.stan.nework.providers.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.stan.nework.domain.models.network.PushToken
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.models.network.event.EventRequest
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.domain.models.network.post.MediaResponse
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.models.ui.post.MediaModel
import ru.stan.nework.domain.models.ui.post.Post

interface NetworkService {

    @GET("api/posts")
    suspend fun getPosts(): List<PostModel>

    @GET("api/posts/latest")
    suspend fun getLatest(@Query("count") count: Int): Response<List<Post>>

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

    @GET("api/my/wall")
    suspend fun myWall(): List<PostModel>

    @GET("api/events")
    suspend fun getEvents(): List<EventModel>

    @POST("api/events")
    suspend fun addEvent(@Body event: EventRequest): EventModel

    @DELETE("api/events/{id}")
    suspend fun removeEvent(@Path("id") id: Long): EventModel

    @GET("api/events/{event_id}")
    suspend fun getEventById(@Path("event_id") id: Long): EventModel

    @POST("api/events/{event_id}/likes")
    suspend fun likeEventById(@Path("event_id") id: Long): EventModel

    @DELETE("api/events/{event_id}/likes")
    suspend fun deleteEventLike(@Path("event_id") id: Long): EventModel

    @GET("api/events/latest")
    suspend fun getLatestEvents(@Query("count") count: Int): Response<List<Event>>

    @GET("api/events/{id}/before")
    suspend fun getBeforeEvents(
        @Path("id") id: Long,
        @Query("count") count: Int,
    ): Response<List<Event>>

    @GET("api/events/{id}/after")
    suspend fun getAfterEvents(
        @Path("id") id: Long,
        @Query("count") count: Int,
    ): Response<List<Event>>

    @GET("api/{id}/jobs")
    suspend fun getJobById(@Path("id") id: Long): List<Job>

    @GET("api/my/jobs")
    suspend fun getMyJobs(): List<Job>

    @POST("api/my/jobs")
    suspend fun saveJob(@Body job: Job): Job

    @DELETE("api/my/jobs/{id}")
    suspend fun removeJobById(@Path("id") id: Long): Response<Unit>

    @POST("api/users/push-tokens")
    suspend fun sendPushToken(@Body pushToken: PushToken): Response<Unit>
}