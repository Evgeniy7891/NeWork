package ru.stan.nework.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.usecase.auth.AuthUserUseCase
import ru.stan.nework.providers.network.AppAuth
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val authUserUseCase: AuthUserUseCase,
    private val auth: AppAuth
) : ViewModel() {

    fun authentication(login: String, password: String,) = viewModelScope.launch {
        when (val response = authUserUseCase.invoke(login, password)) {
            is NetworkState.Success -> response.success.token?.let {
                auth.setAuth(response.success.id, response.success.token, response.success.name, response.success.avatar)
            }
            is NetworkState.Error -> throw RuntimeException("Error ${response.throwable}")
            else -> {}
        }
    }
}