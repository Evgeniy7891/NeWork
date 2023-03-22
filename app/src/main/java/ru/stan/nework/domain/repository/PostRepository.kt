package ru.stan.nework.domain.repository

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post

interface PostRepository {
suspend fun getPosts() : NetworkState<List<Post>>
}