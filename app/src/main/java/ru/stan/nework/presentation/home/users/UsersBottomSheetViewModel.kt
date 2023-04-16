package ru.stan.nework.presentation.home.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.post.GetMarkedUserUseCase
import javax.inject.Inject

@HiltViewModel
class UsersBottomSheetViewModel @Inject constructor(
    private val getMarkedUserUseCase: GetMarkedUserUseCase
) : ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

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
    private fun addUserToList(){
        _idUsers.value = mentions

    }
    fun emptyList(){
        _idUsers.value = kotlin.collections.emptyList()
        idUsers.value = kotlin.collections.emptyList()
    }
}