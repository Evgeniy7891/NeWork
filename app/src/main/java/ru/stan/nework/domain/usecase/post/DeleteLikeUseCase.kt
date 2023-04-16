package ru.stan.nework.domain.usecase.post

import ru.stan.nework.domain.repository.PostRepository
import javax.inject.Inject

class DeleteLikeUseCase @Inject constructor(private val repository: PostRepository){
    suspend operator fun invoke(id: Long) {
        return repository.deleteLikeById(id)
    }
}