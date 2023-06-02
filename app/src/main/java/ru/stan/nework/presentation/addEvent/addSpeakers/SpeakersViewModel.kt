package ru.stan.nework.presentation.addEvent.addSpeakers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.users.GetUsersUseCase
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SpeakersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel() {

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
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> emit(response.success)
        }
    }
    fun check(id: Int) {
        users.value.forEach {
            if (it.id == id) it.isChecked = true
        }
    }

    fun uncheck(id: Int) {
        users.value.forEach {
            if (it.id == id) it.isChecked = false
        }
    }
    fun addUsers() {
        val listChecked = mutableListOf<Int>()
        val userList = mutableListOf<UserUI>()
        users.value.forEach { user ->
            if(user.isChecked) {
                listChecked.add(user.id)
                userList.add(user)
            }
        }
        _idUsers.value = listChecked
    }
}