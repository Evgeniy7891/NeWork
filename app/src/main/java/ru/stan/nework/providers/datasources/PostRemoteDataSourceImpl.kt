package ru.stan.nework.providers.datasources

import okhttp3.MultipartBody
import retrofit2.http.Path
import ru.stan.nework.data.datasources.PostRemoteDataSource
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post
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
        println("DATA SOURCE ${response}")
        return Attachment(type, response.url)
    }
    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }
    override suspend fun getUser(id: Long): User {
        return apiService.getUserById(id)
    }
    override suspend fun removeById(id: Long) {
        apiService.removeById(id)
    }
    override suspend fun getPost(id: Long): PostModel {
        return apiService.getPostById(id)
    }
    override suspend fun likeById(id: Long): PostModel {
       return apiService.likeById(id)
    }
    override suspend fun deleteLikeById(id: Long): PostModel {
       return apiService.deleteLike(id)
    }

    override suspend fun userWall(id: Long): List<PostModel> {
        return apiService.userWall(id)
    }
}