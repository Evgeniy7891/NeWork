package ru.stan.nework.presentation.home.users

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.post.GetMarkedUserUseCase
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class UsersBottomSheetViewModel @Inject constructor(
    private val getMarkedUserUseCase: GetMarkedUserUseCase
) : BaseViewModel() {

    private val _idUsers = MutableLiveData<List<UserUI>>()
    val idUsers: MutableLiveData<List<UserUI>>
        get() = _idUsers

    private var mentions = mutableListOf<UserUI>()

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
    private fun addUserToList(){
        _idUsers.value = mentions

    }
    fun emptyList(){
        _idUsers.value = kotlin.collections.emptyList()
        idUsers.value = kotlin.collections.emptyList()
    }
}