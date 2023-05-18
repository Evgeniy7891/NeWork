package ru.stan.nework.presentation.account.pager.settings

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
import ru.stan.nework.domain.usecase.jobs.AddJobUseCase
import ru.stan.nework.providers.network.AppAuth
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel@Inject constructor(
    private val addJobUseCase: AddJobUseCase,
    appAuth: AppAuth
) : ViewModel() {

    val myId: Long = appAuth.authStateFlow.value.id

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun addJob(job: Job) = viewModelScope.launch {
        when (val response = addJobUseCase.invoke(job)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> {
                println("JOB $job")
            }
        }
    }
}