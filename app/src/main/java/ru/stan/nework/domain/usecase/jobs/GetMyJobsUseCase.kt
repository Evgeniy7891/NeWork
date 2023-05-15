package ru.stan.nework.domain.usecase.jobs

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.domain.repository.JobsRepository
import javax.inject.Inject

class GetMyJobsUseCase @Inject constructor(private val repository: JobsRepository){

    suspend operator fun invoke(): NetworkState<List<Job>> {
        return repository.getMyJobs()
    }
}