package ru.stan.nework.presentation.usersProfile.pager.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.usecase.post.GetUserPostsUseCase
import javax.inject.Inject

@HiltViewModel
class UserPostsViewModel@Inject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase
) : ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    private val _posts = MutableSharedFlow<List<Post>>()
    val posts = _posts.asSharedFlow()

   fun getWall(id: Long) = viewModelScope.launch {
       println("ID - $id")
        when (val response = getUserPostsUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> _posts.emit(response.success)
        }
    }
}