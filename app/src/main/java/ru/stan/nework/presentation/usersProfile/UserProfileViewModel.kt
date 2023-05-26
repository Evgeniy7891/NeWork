package ru.stan.nework.presentation.usersProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.jobs.GetUserJobsUseCase
import ru.stan.nework.domain.usecase.users.GetUserByIdUseCase
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel@Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase
): ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    private val _user = MutableSharedFlow<UserUI>()
    val user = _user.asSharedFlow()

    fun getUser(id: Long) = viewModelScope.launch {
        println("ID - $id")
        when (val response = getUserByIdUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> _user.emit(response.success)
        }
    }
}