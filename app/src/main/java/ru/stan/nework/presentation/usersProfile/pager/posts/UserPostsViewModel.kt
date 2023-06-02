package ru.stan.nework.presentation.usersProfile.pager.posts

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.usecase.post.GetUserPostsUseCase
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class UserPostsViewModel@Inject constructor(
    private val getUserPostsUseCase: GetUserPostsUseCase
) : BaseViewModel() {

    private val _posts = MutableSharedFlow<List<Post>>()
    val posts = _posts.asSharedFlow()

   fun getWall(id: Long) = viewModelScope.launch {
        when (val response = getUserPostsUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> _posts.emit(response.success)
        }
    }
}