package ru.stan.nework.data.datasources

import okhttp3.MultipartBody
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.post.AttachmentType

interface PostRemoteDataSource {
    suspend fun getPosts() : List<PostModel>
    suspend fun addPost(post: PostRequest): PostModel
    suspend fun addMultimedia(type: AttachmentType, file: MultipartBody.Part): Attachment
    suspend fun getUsers(): List<User>
    suspend fun getUser(id: Long): User
    suspend fun removeById(id: Long)
    suspend fun getPost(id: Long): PostModel
    suspend fun likeById(id: Long): PostModel
    suspend fun deleteLikeById(id:Long)
}