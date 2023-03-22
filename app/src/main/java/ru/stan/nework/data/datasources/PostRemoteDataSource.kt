package ru.stan.nework.data.datasources

import ru.stan.nework.domain.models.network.post.PostModel

interface PostRemoteDataSource {

    suspend fun getPosts() : List<PostModel>
}