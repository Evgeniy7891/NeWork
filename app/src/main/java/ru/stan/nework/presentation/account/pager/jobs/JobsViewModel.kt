package ru.stan.nework.presentation.account.pager.jobs

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.domain.usecase.jobs.GetMyJobsUseCase
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class JobsViewModel @Inject constructor(
    private val getMyJobsUseCase: GetMyJobsUseCase,
) : BaseViewModel() {

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