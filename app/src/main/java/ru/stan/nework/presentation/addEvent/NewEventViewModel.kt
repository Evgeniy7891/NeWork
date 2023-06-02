package ru.stan.nework.presentation.addEvent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.event.EventRequest
import ru.stan.nework.domain.models.network.event.Type
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.events.AddEventUseCase
import ru.stan.nework.domain.usecase.events.GetEventByIdUseCase
import ru.stan.nework.domain.usecase.post.GetMarkedUserUseCase
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

val editedEvent = EventRequest(
    id = 0,
    content = "",
    datetime = null,
    type = Type.OFFLINE,
    attachment = null,
    link = null,
    speakerIds = listOf()
)
val speakers = mutableListOf<User>()

@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val addEventUseCase: AddEventUseCase,
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val getMarkedUserUseCase: GetMarkedUserUseCase
) : BaseViewModel() {

    val newEvent: MutableLiveData<EventRequest> = MutableLiveData(editedEvent)

    val speakersData: MutableLiveData<MutableList<User>> = MutableLiveData()

    private val _idUsers = MutableLiveData<Set<UserUI>>()
    val idUsers: MutableLiveData<Set<UserUI>>
        get() = _idUsers

    private var mentions = mutableSetOf<UserUI>()

    fun createEvent(event: EventRequest) {
        viewModelScope.launch {
            when (val response = addEventUseCase.invoke(event)) {
                is NetworkState.Success ->  deleteEditEvent()
                is NetworkState.Error -> throw RuntimeException("Error ${response.throwable}")
                else -> _isLoading.emit(true)
            }
        }
    }

    fun addLink(link: String) {
        if (link != "") {
            newEvent.value = newEvent.value?.copy(link = link)
        } else {
            newEvent.value = newEvent.value?.copy(link = null)
        }
    }

    fun addDateAndTime(dateAndTime: String) {
        newEvent.value = newEvent.value?.copy(datetime = dateAndTime)
    }

    fun addUsrsId(usersId: List<Int>) {
        newEvent.value = newEvent.value?.copy(speakerIds = usersId)
    }

    fun deleteEditEvent() {
        newEvent.postValue(editedEvent)
        speakers.clear()
        speakersData.postValue(speakers)
    }

    fun getUser(id: Long) = viewModelScope.launch {
        when (val response = getMarkedUserUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> {
                mentions.add(response.success)
                addUserToList()
            }
        }
    }

    private fun addUserToList() {
        _idUsers.value = mentions
    }

    fun emptyList() {
        _idUsers.value = emptySet()
        idUsers.value = emptySet()
    }

    fun eventInit(id: Int) = viewModelScope.launch {
        when (val response = getEventByIdUseCase.invoke(id.toLong())) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> {
                newEvent.value = response.success.id.let { id ->
                    response.success.content?.let { content ->
                        response.success.speakerIds.let { speakers ->
                            newEvent.value?.copy(
                                id = id,
                                content = content,
                                link = response.success.link,
                                datetime = response.success.datetime,
                                speakerIds = speakers
                            )
                        }
                    }
                }
            }
        }
    }
}