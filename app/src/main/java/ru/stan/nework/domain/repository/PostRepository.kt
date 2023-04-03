package ru.stan.nework.domain.repository

import okhttp3.MultipartBody
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post

interface PostRepository {
suspend fun getPosts() : NetworkState<List<Post>>
suspend fun addPost(post: PostRequest) : NetworkState<PostModel>
suspend fun addMultimedia(type: AttachmentType, file: MultipartBody.Part): NetworkState<Attachment>
suspend fun getUsers(): NetworkState<List<User>>
}