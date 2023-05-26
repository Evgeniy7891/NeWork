package ru.stan.nework.presentation.usersProfile.pager.jobs

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
import ru.stan.nework.domain.usecase.jobs.GetUserJobsUseCase
import javax.inject.Inject

@HiltViewModel
class UserJobsViewModel@Inject constructor(
    private val getUserJobsUseCase: GetUserJobsUseCase
) : ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    private val _jobs = MutableSharedFlow<List<Job>>()
    val jobs = _jobs.asSharedFlow()

    fun getJobs(id: Long) = viewModelScope.launch {
        println("ID - $id")
        when (val response = getUserJobsUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> _jobs.emit(response.success)
        }
    }
}