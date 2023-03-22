package ru.stan.nework.providers.network

import retrofit2.http.GET
import ru.stan.nework.domain.models.network.post.PostModel

interface NetworkService {

    @GET("api/posts/")
    suspend fun getPosts() : List<PostModel>
}