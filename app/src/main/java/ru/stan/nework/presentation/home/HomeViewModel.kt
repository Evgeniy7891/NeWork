package ru.stan.nework.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.usecase.post.GetPostsUseCase
import ru.stan.nework.domain.usecase.post.RemovePostUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val removePostUseCase: RemovePostUseCase
): ViewModel() {

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    val posts: StateFlow<List<Post>> =
        getNewsList().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )
    private fun getNewsList() = flow {
        when (val response = getPostsUseCase.invoke()) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> emit(response.success)
        }
    }
   fun deletePost(id: Long) = viewModelScope.launch {
       removePostUseCase.invoke(id)
   }
}