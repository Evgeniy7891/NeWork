package ru.stan.nework.domain.usecase.account

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.repository.AccountRepository
import javax.inject.Inject

class GetMyWallUseCase @Inject constructor(private val repository: AccountRepository){
    suspend operator fun invoke(): NetworkState<List<Post>> {
        return repository.getMyWall()
    }
}