package ru.stan.nework.providers.datasources

import ru.stan.nework.data.datasources.JobsRemoteDataSource
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.providers.network.NetworkService
import javax.inject.Inject

class JobsRemoteDataSourceImpl @Inject constructor(private val apiService: NetworkService) : JobsRemoteDataSource {

    override suspend fun getJob(id: Long): List<Job> {
       return apiService.getJobById(id)
    }

    override suspend fun saveJob(job: Job): Job {
        return apiService.saveJob(job)
    }

    override suspend fun removeJob(id: Long) {
        apiService.removeJobById(id)
    }

    override suspend fun getMyJobs(): List<Job> {
       return apiService.getMyJobs()
    }

    override suspend fun getUserJobs(id: Long): List<Job> {
        return apiService.getUserJobs(id)
    }
}