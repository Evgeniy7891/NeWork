package ru.stan.nework.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.usecase.events.AddEventUseCase
import ru.stan.nework.domain.usecase.events.AddLikeUseCase
import ru.stan.nework.domain.usecase.events.DeleteLikeUseCase
import ru.stan.nework.domain.usecase.events.GetEventsUseCase
import ru.stan.nework.domain.usecase.events.RemoveEventUseCase
import javax.inject.Inject

@HiltViewModel
class EventsWallViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    private val removeEventUseCase: RemoveEventUseCase,
    private val addLikeUseCase: AddLikeUseCase
) : ViewModel() {

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

    private fun getEventsList() = flow {
        when (val response = getEventsUseCase.invoke()) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> emit(response.success)
        }
    }

    fun removeEvent(id: Long) = viewModelScope.launch{
        when (val response = removeEventUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> getEventsList()
        }
    }
}