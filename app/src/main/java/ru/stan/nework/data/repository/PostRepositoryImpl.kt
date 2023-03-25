package ru.stan.nework.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import ru.stan.nework.data.datasources.PostRemoteDataSource
import ru.stan.nework.di.IoDispatcher
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.repository.PostRepository
import ru.stan.nework.utils.safeApiCall
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteDataSource: PostRemoteDataSource
) : PostRepository {

    override suspend fun getPosts(): NetworkState<List<Post>> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getPosts()
                .map { it.convertTo() }
        }

    }

}