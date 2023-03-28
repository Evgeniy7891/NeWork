package ru.stan.nework.providers.datasources

import ru.stan.nework.data.datasources.AuthRemoteDataSource
import ru.stan.nework.providers.network.AuthState
import ru.stan.nework.providers.network.NetworkService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val apiService: NetworkService) : AuthRemoteDataSource {

    override suspend fun register(login: String, password: String, name: String): AuthState {
       return apiService.register(login, password, name)
    }

    override suspend fun authentication(login: String, password: String): AuthState {
        return apiService.authentication(login, password)
    }
}