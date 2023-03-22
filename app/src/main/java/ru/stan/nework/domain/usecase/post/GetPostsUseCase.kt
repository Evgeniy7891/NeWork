package ru.stan.nework.domain.usecase.post

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.repository.PostRepository
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(private val repository: PostRepository){
    suspend operator fun invoke(): NetworkState<List<Post>> {
        return repository.getPosts()
    }
}