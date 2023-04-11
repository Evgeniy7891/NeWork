package ru.stan.nework.presentation.addPost

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.MediaModel
import ru.stan.nework.domain.models.ui.user.UserUI
import ru.stan.nework.domain.usecase.post.AddMultiMediaUseCase
import ru.stan.nework.domain.usecase.post.AddPostUseCase
import ru.stan.nework.domain.usecase.post.GetPostByIdUseCase
import ru.stan.nework.domain.usecase.post.GetUsersUseCase
import java.io.File
import javax.inject.Inject

private val editedPost = PostRequest(
    id = 0,
    content = "",
    link = null,
    attachment = null,
    mentionIds = mutableListOf()
)
private val noMedia = MediaModel()

@HiltViewModel
class PostViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val addMultiMediaUseCase: AddMultiMediaUseCase,
    private val getPostByIdUseCase: GetPostByIdUseCase
) : ViewModel() {

    private val newPost: MutableLiveData<PostRequest> = MutableLiveData(editedPost)

    private val _media = MutableLiveData(noMedia)
    val media: LiveData<MediaModel>
        get() = _media

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun createPost(content: String) {
        newPost.value = newPost.value?.copy(content = content)
        val post = newPost.value!!
        println("ViewModel Create - ${post}")
        viewModelScope.launch {
            when (val response = addPostUseCase.invoke(post)) {
                is NetworkState.Success -> {
                    deleteEditPost()
                }
                is NetworkState.Error -> throw RuntimeException("Error ${response.throwable}")
                else -> {}
            }
        }
    }

    fun changeMedia(uri: Uri?, file: File?, type: AttachmentType?) {
        _media.value = MediaModel(uri, file, type)
    }

    fun addMediaToPost(
        type: AttachmentType,
        file: MultipartBody.Part,
    ) {
        viewModelScope.launch {
            when (val response = addMultiMediaUseCase.invoke(type, file)) {
                is NetworkState.Success -> {
                    newPost.value = newPost.value?.copy(attachment = response.success)
                    println("ViewModel addMedia - ${response.success}")
                }
                is NetworkState.Error -> throw RuntimeException("Error ${response.throwable}")
                else -> {}
            }
        }
    }

    fun addUsrsId(usersId: List<Int>) {
        newPost.value = newPost.value?.copy(mentionIds = usersId)
    }

    private fun deleteEditPost() {
        newPost.value = editedPost
    }

    fun postInit(id: Int) = viewModelScope.launch {
        when (val response = getPostByIdUseCase.invoke(id.toLong())) {
            is NetworkState.Error -> _errorMessage.emit(response.throwable)
            is NetworkState.Loading -> TODO("not implemented yet")
            is NetworkState.Success -> {
                newPost.value = newPost.value?.copy(
                    id = response.success.id,
                    content = response.success.content,
                    link = response.success.link,
                    mentionIds = response.success.mentionIds
                )
                if(response.success.attachment?.type != null) {
                    newPost.value = newPost.value?.copy(attachment = response.success.attachment)
                }
                println("postInit - ${response.success.attachment?.type}")
                println("postInit - ${response.success.attachment?.url}")
            }
        }
    }
}