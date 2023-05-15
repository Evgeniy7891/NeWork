package ru.stan.nework.domain.usecase.jobs

import ru.stan.nework.domain.repository.JobsRepository
import javax.inject.Inject

class RemoveJobUseCase @Inject constructor(private val repository: JobsRepository) {

    suspend operator fun invoke(id: Long) {
        return repository.removeJob(id)
    }
}