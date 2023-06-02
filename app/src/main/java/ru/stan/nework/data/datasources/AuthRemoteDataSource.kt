package ru.stan.nework.data.datasources

import ru.stan.nework.providers.network.AuthState

interface AuthRemoteDataSource {
    suspend fun register(login: String, password: String, name: String) : AuthState
    suspend fun authentication(login: String, password: String): AuthState
}