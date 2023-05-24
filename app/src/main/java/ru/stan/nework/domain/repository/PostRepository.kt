package ru.stan.nework.domain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import retrofit2.http.Path
import ru.stan.nework.data.room.entity.UserPreview
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.models.ui.user.UserUI

interface PostRepository {

    val data: Flow<PagingData<Post>>

    val postUsersData: MutableLiveData<List<UserPreview>>
    suspend fun getPosts(): NetworkState<List<Post>>
    suspend fun addPost(post: PostRequest): NetworkState<PostModel>
    suspend fun addMultimedia(type: AttachmentType, file: MultipartBody.Part): NetworkState<Attachment>
    suspend fun getUsers(): NetworkState<List<UserUI>>
    suspend fun getUser(id: Long): NetworkState<UserUI>
    suspend fun removeById(id: Long)
    suspend fun getPost(id: Long): NetworkState<Post>
    suspend fun likeById(id: Long): NetworkState<Post>
    suspend fun deleteLikeById(id: Long)
    suspend fun userWall(id: Long): NetworkState<List<Post>>
}