package ru.stan.nework.domain.usecase.users

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.repository.PostRepository
import javax.inject.Inject

class GetUserByIdUseCase  @Inject constructor(private val repository: PostRepository){
    suspend operator fun invoke(id: Long): NetworkState<UserUI> {
        return repository.getUser(id)
    }
}