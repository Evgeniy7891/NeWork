package ru.stan.nework.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.usecase.events.GetEventsUseCase
import javax.inject.Inject

@HiltViewModel
class EventsWallViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase) : ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

        val events: StateFlow<List<Event>> =
        getEventsList().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
   fun getEventsList() = flow {
        when (val response = getEventsUseCase.invoke()) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> emit(response.success)
        }
    }
}