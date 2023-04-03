package ru.stan.nework.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.MultipartBody
import ru.stan.nework.data.datasources.PostRemoteDataSource
import ru.stan.nework.di.IoDispatcher
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.post.AttachmentType
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
    override suspend fun addPost(post: PostRequest): NetworkState<PostModel> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.addPost(post)
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
    override suspend fun getUsers(): NetworkState<List<User>> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getUsers()
        }
    }
}