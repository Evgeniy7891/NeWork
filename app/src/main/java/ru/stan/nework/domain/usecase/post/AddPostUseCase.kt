package ru.stan.nework.domain.usecase.post

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.repository.PostRepository
import javax.inject.Inject

class AddPostUseCase  @Inject constructor(private val repository: PostRepository){

    suspend operator fun invoke(post: PostRequest): NetworkState<PostModel> {
        return repository.addPost(post)
    }
}