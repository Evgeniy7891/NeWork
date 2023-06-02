package ru.stan.nework.presentation.usersProfile.pager.jobs

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.job.Job
import ru.stan.nework.domain.usecase.jobs.GetUserJobsUseCase
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class UserJobsViewModel@Inject constructor(
    private val getUserJobsUseCase: GetUserJobsUseCase
) : BaseViewModel() {

    private val _jobs = MutableSharedFlow<List<Job>>()
    val jobs = _jobs.asSharedFlow()

    fun getJobs(id: Long) = viewModelScope.launch {
        when (val response = getUserJobsUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> _jobs.emit(response.success)
        }
    }
}