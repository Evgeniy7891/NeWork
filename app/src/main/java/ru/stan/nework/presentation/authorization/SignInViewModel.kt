package ru.stan.nework.presentation.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.usecase.auth.RegisterUserUseCase
import ru.stan.nework.providers.network.AppAuth
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val auth: AppAuth
) : ViewModel() {

    fun register(login: String, password: String, name: String) = viewModelScope.launch {
        when (val response = registerUserUseCase.invoke(login, password, name)) {
            is NetworkState.Success -> response.success.token?.let {
            auth.setAuth(response.success.id, response.success.token, response.success.name, response.success.avatar)
            }
            is NetworkState.Error -> throw RuntimeException("Error ${response.throwable}")
            else -> {}
        }
    }
}