package ru.stan.nework.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.users.GetUserByIdUseCase
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.providers.network.AuthState
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel@Inject constructor(
    appAuth: AppAuth,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : BaseViewModel() {

    val data: LiveData<AuthState> = appAuth
        .authStateFlow
        .asLiveData(Dispatchers.Default)

    private val _user = MutableSharedFlow<UserUI>()
    val user = _user.asSharedFlow()

    fun getUser(id: Long) = viewModelScope.launch {
        when (val response = getUserByIdUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> _user.emit(response.success)
        }
    }
}