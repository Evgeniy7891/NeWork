package ru.stan.nework.presentation.addEvent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.event.EventRequest
import ru.stan.nework.domain.models.network.event.Type
import ru.stan.nework.domain.models.network.user.User
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.events.AddEventUseCase
import ru.stan.nework.domain.usecase.events.GetEventByIdUseCase
import ru.stan.nework.domain.usecase.post.GetMarkedUserUseCase
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
) : ViewModel() {

    val newEvent: MutableLiveData<EventRequest> = MutableLiveData(editedEvent)
    val usersList: MutableLiveData<List<User>> = MutableLiveData()
    val speakersData: MutableLiveData<MutableList<User>> = MutableLiveData()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun createEvent(event: EventRequest) {
        viewModelScope.launch {
            when (val response = addEventUseCase.invoke(event)) {
                is NetworkState.Success -> {
                    deleteEditEvent()
                }

                is NetworkState.Error -> throw RuntimeException("Error ${response.throwable}")
                else -> {}
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

    fun check(id: Int) {
        usersList.value?.forEach {
            if (it.id == id) {
                it.isChecked = true
            }
        }
    }

    fun unCheck(id: Int) {
        usersList.value?.forEach {
            if (it.id == id) {
                it.isChecked = false
            }
        }
    }

    fun deleteEditEvent() {
        newEvent.postValue(editedEvent)
        speakers.clear()
        speakersData.postValue(speakers)
    }

    private val _idUsers = MutableLiveData<List<UserUI>>()
    val idUsers: MutableLiveData<List<UserUI>>
        get() = _idUsers

    private var mentions = mutableListOf<UserUI>()

    fun getUser(id: Long) = viewModelScope.launch {
        when (val response = getMarkedUserUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> println("VIEWMODEL USER BOTTOM")
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
        _idUsers.value = kotlin.collections.emptyList()
        idUsers.value = kotlin.collections.emptyList()
    }

    fun eventInit(id: Int) = viewModelScope.launch {
        when (val response = getEventByIdUseCase.invoke(id.toLong())) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
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