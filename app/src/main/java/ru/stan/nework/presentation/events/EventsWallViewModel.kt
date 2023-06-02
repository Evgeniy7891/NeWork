package ru.stan.nework.presentation.events

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.repository.EventsRepository
import ru.stan.nework.domain.usecase.events.AddLikeUseCase
import ru.stan.nework.domain.usecase.events.DeleteLikeUseCase
import ru.stan.nework.domain.usecase.events.GetEventsUseCase
import ru.stan.nework.domain.usecase.events.RemoveEventUseCase
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class EventsWallViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    private val removeEventUseCase: RemoveEventUseCase,
    private val addLikeUseCase: AddLikeUseCase,
    appAuth: AppAuth,
    private val eventsRepository: EventsRepository
) : BaseViewModel() {


    val data: Flow<PagingData<Event>> = appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            val cached = eventsRepository.data.cachedIn(viewModelScope)
            cached.map { pagingData ->
                pagingData.map {
                    it.copy(ownedByMe = it.authorId.toLong() == myId)
                }
            }
        }

    val events: StateFlow<List<Event>> =
        getEventsList().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private fun getEventsList() = flow {
        when (val response = getEventsUseCase.invoke()) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> emit(response.success)
        }
    }

    fun removeEvent(id: Long) = viewModelScope.launch{
        when (val response = removeEventUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> getEventsList()
        }
    }

    fun deleteLike(id: Long) = viewModelScope.launch {
        deleteLikeUseCase.invoke(id)
    }
    fun likeById(id:Long) = viewModelScope.launch {
        when(val response = addLikeUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> true
        }
    }
}