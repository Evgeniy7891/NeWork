package ru.stan.nework.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import ru.stan.nework.data.datasources.AuthRemoteDataSource
import ru.stan.nework.di.IoDispatcher
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.repository.AuthRepository
import ru.stan.nework.providers.network.AuthState
import ru.stan.nework.utils.safeApiCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun register(
        login: String,
        password: String,
        name: String
    ): NetworkState<AuthState> {
        return safeApiCall(ioDispatcher) {
            authRemoteDataSource.register(login, password, name)
        }
    }

    override suspend fun authentication(login: String, password: String): NetworkState<AuthState> {
        return safeApiCall(ioDispatcher) {
            authRemoteDataSource.authentication(login, password)
        }
    }
}