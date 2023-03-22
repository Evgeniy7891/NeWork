package ru.stan.nework.providers.datasources

import ru.stan.nework.data.datasources.PostRemoteDataSource
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.providers.network.NetworkService
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(private val apiService: NetworkService) :
    PostRemoteDataSource {

    override suspend fun getPosts(): List<PostModel> {
        return apiService.getPosts()
    }
}