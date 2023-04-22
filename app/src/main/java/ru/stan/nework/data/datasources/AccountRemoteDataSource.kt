package ru.stan.nework.data.datasources

import ru.stan.nework.domain.models.network.post.PostModel

interface AccountRemoteDataSource {

    suspend fun getMyWall() : List<PostModel>
}