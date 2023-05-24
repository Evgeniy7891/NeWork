package ru.stan.nework.domain.repository

import retrofit2.http.Path
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.job.Job

interface JobsRepository {

    suspend fun getJob(id: Long): NetworkState<List<Job>>
    suspend fun saveJob(job: Job): NetworkState<Job>
    suspend fun removeJob(id: Long)
    suspend fun getMyJobs(): NetworkState<List<Job>>
    suspend fun getUserJobs(id: Long): NetworkState<List<Job>>
}