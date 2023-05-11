package ru.stan.nework.data.repository.posts

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import ru.stan.nework.data.datasources.PostRemoteDataSource
import ru.stan.nework.data.room.dao.PostDao
import ru.stan.nework.data.room.entity.PostEntity
import ru.stan.nework.data.room.entity.UserPreview
import ru.stan.nework.di.IoDispatcher
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.repository.PostRepository
import ru.stan.nework.presentation.addPost.PostFragment
import ru.stan.nework.utils.safeApiCall
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteDataSource: PostRemoteDataSource,
    mediator: PostRemoteMediator,
    private val dao: PostDao,
) : PostRepository {

    override val data: Flow<PagingData<Post>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false, initialLoadSize = 15),
            pagingSourceFactory = { dao.getAllPosts() },
            remoteMediator = mediator
        ).flow.map {
            it.map(PostEntity::toDto)
        }

    override val postUsersData: MutableLiveData<List<UserPreview>> = MutableLiveData(emptyList())

    override suspend fun getPosts(): NetworkState<List<Post>> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getPosts()
                .map { it.convertTo() }
        }
    }

    override suspend fun addPost(post: PostRequest): NetworkState<PostModel> {

        return safeApiCall(ioDispatcher) {
           val body = remoteDataSource.addPost(post)
            dao.insert(PostEntity.fromDto(body.convertTo()))
            body
        }
    }

    override suspend fun addMultimedia(
        type: AttachmentType,
        file: MultipartBody.Part
    ): NetworkState<Attachment> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.addMultimedia(type, file)
        }
    }

    override suspend fun getUsers(): NetworkState<List<UserUI>> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getUsers()
                .map { it.convertTo() }
        }
    }

    override suspend fun getUser(id: Long): NetworkState<UserUI> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getUser(id).convertTo()
        }
    }

    override suspend fun removeById(id: Long) {
        safeApiCall(ioDispatcher) {
            remoteDataSource.removeById(id)
            dao.removeById(id.toInt())
        }
    }

    override suspend fun getPost(id: Long): NetworkState<Post> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getPost(id).convertTo()
        }
    }

    override suspend fun likeById(id: Long): NetworkState<Post> {
        return safeApiCall(ioDispatcher) {
            val body = remoteDataSource.likeById(id)
            dao.insert(PostEntity.fromDto(body.convertTo()))
            body.convertTo()
        }
    }

    override suspend fun deleteLikeById(id: Long) {
        safeApiCall(ioDispatcher) {
            val body = remoteDataSource.deleteLikeById(id).convertTo()
            dao.insert(PostEntity.fromDto(body))
        }
    }
}