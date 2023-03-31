package ru.stan.nework.providers.datasources

import okhttp3.MultipartBody
import ru.stan.nework.data.datasources.PostRemoteDataSource
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.providers.network.NetworkService
import javax.inject.Inject

class PostRemoteDataSourceImpl @Inject constructor(private val apiService: NetworkService) :
    PostRemoteDataSource {

    override suspend fun getPosts(): List<PostModel> {
        return apiService.getPosts()
    }
    override suspend fun addPost(post: PostRequest): PostModel {
        return apiService.addPost(post)
    }
    override suspend fun addMultimedia(type: AttachmentType, file: MultipartBody.Part): Attachment {
        val response = apiService.addMultimedia(file)
        return Attachment(type, response.url)
    }
}