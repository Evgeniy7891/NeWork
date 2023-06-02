package ru.stan.nework.presentation.account.pager.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.domain.usecase.jobs.AddJobUseCase
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel@Inject constructor(
    private val addJobUseCase: AddJobUseCase,
    appAuth: AppAuth
) : BaseViewModel() {

    val myId: Long = appAuth.authStateFlow.value.id

    fun addJob(job: Job) = viewModelScope.launch {
        when (val response = addJobUseCase.invoke(job)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> response.success
        }
    }
}