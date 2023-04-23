package ru.stan.nework.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import ru.stan.nework.data.datasources.AccountRemoteDataSource
import ru.stan.nework.di.IoDispatcher
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.repository.AccountRepository
import ru.stan.nework.utils.safeApiCall
import javax.inject.Inject

class AccountRepositoryImpl@Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteDataSource: AccountRemoteDataSource
) : AccountRepository {
    override suspend fun getMyWall(): NetworkState<List<Post>> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getMyWall()
                .map { it.convertTo() }
        }
    }
}