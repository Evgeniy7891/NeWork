package ru.stan.nework.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.post.Post
import ru.stan.nework.domain.usecase.post.DeleteLikeUseCase
import ru.stan.nework.domain.usecase.post.GetPostByIdUseCase
import ru.stan.nework.domain.usecase.post.GetPostsUseCase
import ru.stan.nework.domain.usecase.post.LikeByIdUseCase
import ru.stan.nework.domain.usecase.post.RemovePostUseCase
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.utils.BaseViewModel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val removePostUseCase: RemovePostUseCase,
    private val likeByIdUseCase: LikeByIdUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase,
    private val deleteLikeUseCase: DeleteLikeUseCase,
    auth: AppAuth
): BaseViewModel() {

    private val cached = getPostsUseCase.invoke().cachedIn(viewModelScope)

    val data: Flow<PagingData<Post>> = auth.authStateFlow
        .flatMapLatest { (myId, ) ->
            cached.map {posts ->
                posts.map { it.copy(ownedByMe = it.authorId.toLong() == myId)}
            }
        }.flowOn(Dispatchers.Default)

    val post: MutableLiveData<List<Int>?>
        get() = _post

    private val _post = MutableLiveData<List<Int>?>()

    private var mentionsId = mutableListOf<Int>()

   fun deletePost(id: Long) = viewModelScope.launch {
       removePostUseCase.invoke(id)
   }
    fun deleteLike(id: Long) = viewModelScope.launch {
        deleteLikeUseCase.invoke(id)
    }
    fun likeById(id:Long) = viewModelScope.launch {
        when(val response = likeByIdUseCase.invoke(id)) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
            is NetworkState.Success -> true
        }
    }
    fun getPost(id: Int) = viewModelScope.launch {
        when (val response = getPostByIdUseCase.invoke(id.toLong())) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> _isLoading.emit(true)
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