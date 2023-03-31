package ru.stan.nework.data.datasources

import okhttp3.MultipartBody
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.ui.post.AttachmentType

interface PostRemoteDataSource {
    suspend fun getPosts() : List<PostModel>

    suspend fun addPost(post: PostRequest): PostModel

    suspend fun addMultimedia(type: AttachmentType, file: MultipartBody.Part): Attachment
}