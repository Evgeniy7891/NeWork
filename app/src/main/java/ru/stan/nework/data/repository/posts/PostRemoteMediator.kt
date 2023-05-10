package ru.stan.nework.data.repository.posts

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import retrofit2.HttpException
import ru.stan.nework.data.room.AppDb
import ru.stan.nework.data.room.dao.PostDao
import ru.stan.nework.data.room.dao.PostRemoteKeyDao
import ru.stan.nework.data.room.entity.PostEntity
import ru.stan.nework.data.room.entity.PostRemoteKeyEntity
import ru.stan.nework.data.room.entity.toEntity
import ru.stan.nework.providers.network.NetworkService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val apiService: NetworkService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val db: AppDb
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PostEntity>): MediatorResult {
        try {
            val response = when (loadType) {
            LoadType.REFRESH -> apiService.getLatest(state.config.pageSize)
                LoadType.PREPEND -> {
                    val id = postRemoteKeyDao.max() ?: MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getAfter(id as Long, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getBefore(id as Long, state.config.pageSize)
                }
            }
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
            val body = response.body() ?: throw HttpException(response)

            db.withTransaction {
                when(loadType) {
                    LoadType.REFRESH -> {
                        postRemoteKeyDao.removeAllPosts()
                        postRemoteKeyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    type = PostRemoteKeyEntity.KeyType.AFTER,
                                    id = body.first().id
                                ),
                                PostRemoteKeyEntity(
                                    type = PostRemoteKeyEntity.KeyType.BEFORE,
                                    id = body.last().id,
                                )
                            )
                        )
                        postDao.removeAllPosts()
                    }
                    LoadType.PREPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.AFTER,
                                id = body.first().id
                            )
                        )
                    }
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id
                            ),
                        )
                    }
                }
                postDao.insert(body.toEntity())
            }
            return  MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}
