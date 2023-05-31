package ru.stan.nework.presentation.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _entrance = MutableLiveData<Boolean>()
    val entrance: LiveData<Boolean>
        get() = _entrance

    fun authentication(login: String, password: String,) = viewModelScope.launch {
        when (val response = authUserUseCase.invoke(login, password)) {
            is NetworkState.Success -> response.success.token?.let {
                auth.setAuth(response.success.id, response.success.token, response.success.name, response.success.avatar)
                _entrance.value = true
            }
            is NetworkState.Error -> _entrance.value = false
            else -> {}
        }
    }
}