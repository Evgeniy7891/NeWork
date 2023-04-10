package ru.stan.nework.domain.usecase.post

import ru.stan.nework.domain.repository.PostRepository
import javax.inject.Inject

class RemovePostUseCase @Inject constructor(private val repository: PostRepository){

    suspend operator fun invoke(id: Long) {
        return repository.removeById(id)
    }
}