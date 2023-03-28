package ru.stan.nework.domain.usecase.auth

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.repository.AuthRepository
import ru.stan.nework.providers.network.AuthState
import javax.inject.Inject

class AuthUserUseCase  @Inject constructor(private val authRepository: AuthRepository){

    suspend operator fun invoke(login: String, password: String): NetworkState<AuthState> {
        return authRepository.authentication(login, password)
    }
}