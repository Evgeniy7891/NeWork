package ru.stan.nework.presentation.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.users.GetUsersUseCase
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel() {

    private val _idUsers = MutableLiveData<List<Int>>()
    val idUsers: LiveData<List<Int>>
        get() = _idUsers


    val users: StateFlow<List<UserUI>> =
        getNewsList().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private fun getNewsList() = flow {
        when (val response = getUsersUseCase.invoke()) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> emit(response.success)
        }
    }
}