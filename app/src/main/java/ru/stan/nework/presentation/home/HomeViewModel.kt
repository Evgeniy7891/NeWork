package ru.stan.nework.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.repository.PostRepository
import ru.stan.nework.domain.usecase.post.*
import ru.stan.nework.providers.network.AppAuth
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val removePostUseCase: RemovePostUseCase,
    private val likeByIdUseCase: LikeByIdUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    private val repository: PostRepository,
    auth: AppAuth
): ViewModel() {

    private val cached = repository
        .data
        .cachedIn(viewModelScope)

    val data: Flow<PagingData<Post>> = auth.authStateFlow
        .flatMapLatest { (myId, ) ->
            cached.map {posts ->
                    posts.map { it.copy(ownedByMe = it.authorId.toLong() == myId)}
                }
        }.flowOn(Dispatchers.Default)

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    private val _post = MutableLiveData<List<Int>?>()
    val post: MutableLiveData<List<Int>?>
        get() = _post

    private var mentionsId = mutableListOf<Int>()

//    val posts: StateFlow<List<Post>> =
//        getNewsList().stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.Eagerly,
//            initialValue = emptyList()
//        )
//   fun getNewsList() = flow {
//        when (val response = getPostsUseCase.invoke()) {
//            is NetworkState.Error -> _errorMessage.emit(response.throwable)
//            is NetworkState.Loading -> TODO("not implemented yet")
//            is NetworkState.Success -> emit(response.success)
//        }
//    }
   fun deletePost(id: Long) = viewModelScope.launch {
       removePostUseCase.invoke(id)
   }
    fun deleteLike(id: Long) = viewModelScope.launch {
        deleteLikeUseCase.invoke(id)
    }
    fun likeById(id:Long) = viewModelScope.launch {
        when(val response = likeByIdUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> true
        }
    }
    fun getPost(id: Int) = viewModelScope.launch {
        when (val response = getPostByIdUseCase.invoke(id.toLong())) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> delay(500)
            is NetworkState.Success -> {
                mentionsId = response.success.likeOwnerIds as MutableList<Int>
                addUserToList()
            }
        }
    }
    private fun addUserToList(){
        _post.value = mentionsId
    }
}