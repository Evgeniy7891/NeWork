package ru.stan.nework.domain.repository

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.providers.network.AuthState

interface AuthRepository {

    suspend fun register(login: String, password: String, name: String) : NetworkState<AuthState>

    suspend fun authentication(login: String, password: String): NetworkState<AuthState>
}