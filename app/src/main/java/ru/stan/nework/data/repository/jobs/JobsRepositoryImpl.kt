package ru.stan.nework.data.repository.jobs

import kotlinx.coroutines.CoroutineDispatcher
import ru.stan.nework.data.datasources.JobsRemoteDataSource
import ru.stan.nework.di.IoDispatcher
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.domain.repository.JobsRepository
import ru.stan.nework.utils.safeApiCall
import javax.inject.Inject

class JobsRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val jobsRemoteDataSource: JobsRemoteDataSource
) : JobsRepository {

    override suspend fun getJob(id: Long): NetworkState<List<Job>> {
        return safeApiCall(ioDispatcher){
            jobsRemoteDataSource.getJob(id)
        }
    }

    override suspend fun saveJob(job: Job): NetworkState<Job> {
        return safeApiCall(ioDispatcher){
            jobsRemoteDataSource.saveJob(job)
        }
    }

    override suspend fun removeJob(id: Long) = jobsRemoteDataSource.removeJob(id)

    override suspend fun getMyJobs(): NetworkState<List<Job>> {
       return safeApiCall(ioDispatcher){
           jobsRemoteDataSource.getMyJobs()
       }
    }

    override suspend fun getUserJobs(id: Long): NetworkState<List<Job>> {
        return safeApiCall(ioDispatcher){
            jobsRemoteDataSource.getUserJobs(id)
        }
    }
}