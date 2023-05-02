package ru.stan.nework.presentation.addEvent.addSpeakers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.post.GetUsersUseCase
import javax.inject.Inject

@HiltViewModel
class SpeakersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    private val _idUsers = MutableLiveData<List<Int>>()
    val idUsers: LiveData<List<Int>>
        get() = _idUsers


    val users: StateFlow<List<UserUI>> =
        getNewsList().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private fun getNewsList() = flow {
        when (val response = getUsersUseCase.invoke()) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> emit(response.success)
        }
    }
    fun check(id: Int) {
        users.value.forEach {
            if (it.id == id) it.isChecked = true
        }
        println("VIEW MODEL chech ${users.value}")
    }

    fun uncheck(id: Int) {
        users.value.forEach {
            if (it.id == id) it.isChecked = false
        }
        println("VIEW MODEL unchec ${users.value}")
    }
    fun addUsers() {
        val listChecked = mutableListOf<Int>()
        val userList = mutableListOf<UserUI>()
        users.value.forEach { user ->
            if(user.isChecked) {
                listChecked.add(user.id)
                userList.add(user)
                println("VIEW MODEL user list ${userList}")
            }
        }
        _idUsers.value = listChecked
    }
}