package ru.stan.nework.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.account.GetMyWallUseCase
import ru.stan.nework.domain.usecase.jobs.AddJobUseCase
import ru.stan.nework.domain.usecase.jobs.GetMyJobsUseCase
import ru.stan.nework.domain.usecase.users.GetUserByIdUseCase
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.providers.network.AuthState
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel@Inject constructor(
    appAuth: AppAuth,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getMyWallUseCase: GetMyWallUseCase,
    private val addJobUseCase: AddJobUseCase,
    private val getMyJobsUseCase: GetMyJobsUseCase,
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

    val wall: StateFlow<List<Post>> =
        getMyWall().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    private fun getMyWall() = flow {
        when (val response = getMyWallUseCase.invoke()) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> emit(response.success)
        }
    }

    val myId: Long = appAuth.authStateFlow.value.id

    fun addJob(job: Job) = viewModelScope.launch {
        when (val response = addJobUseCase.invoke(job)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> response.success
        }
    }
    val jobs: StateFlow<List<Job>> =
        getJobsList().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private fun getJobsList() = flow {
        when (val response = getMyJobsUseCase.invoke()) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> emit(response.success)
        }
    }
}