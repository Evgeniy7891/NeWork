package ru.stan.nework.providers.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import ru.stan.nework.domain.models.network.post.PostModel

interface NetworkService {

    @GET("api/posts")
    suspend fun getPosts() : List<PostModel>

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
}