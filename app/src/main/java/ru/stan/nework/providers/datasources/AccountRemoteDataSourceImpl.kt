package ru.stan.nework.providers.datasources

import ru.stan.nework.data.datasources.AccountRemoteDataSource
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.providers.network.NetworkService
import javax.inject.Inject

class AccountRemoteDataSourceImpl @Inject constructor(private val apiService: NetworkService) :
    AccountRemoteDataSource {

    override suspend fun getMyWall(): List<PostModel> {
        return apiService.myWall()
    }
}