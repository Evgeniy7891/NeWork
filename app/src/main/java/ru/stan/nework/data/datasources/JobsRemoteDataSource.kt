package ru.stan.nework.data.datasources

import retrofit2.http.Path
import ru.stan.nework.domain.models.network.job.Job

interface JobsRemoteDataSource {

    suspend fun getJob(id: Long): List<Job>
    suspend fun saveJob(job: Job): Job
    suspend fun removeJob(id: Long)
    suspend fun getMyJobs(): List<Job>
    suspend fun getUserJobs(id: Long): List<Job>
}