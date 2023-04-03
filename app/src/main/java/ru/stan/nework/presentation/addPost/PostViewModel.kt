package ru.stan.nework.presentation.addPost

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.post.PostRequest
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.MediaModel
import ru.stan.nework.domain.usecase.post.AddMultiMediaUseCase
import ru.stan.nework.domain.usecase.post.AddPostUseCase
import java.io.File
import javax.inject.Inject

private val editedPost = PostRequest(
    id = 0,
    content = "",
    link = null,
    attachment = null,
    mentionIds = listOf()
)
private val noMedia = MediaModel()
@HiltViewModel
class PostViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val addMultiMediaUseCase: AddMultiMediaUseCase
) : ViewModel() {

    private val newPost: MutableLiveData<PostRequest> = MutableLiveData(editedPost)

    private val _media = MutableLiveData(noMedia)
    val media: LiveData<MediaModel>
        get() = _media

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
            when(val response = addMultiMediaUseCase.invoke(type, file)) {
                is NetworkState.Success -> {
                    newPost.value = newPost.value?.copy(attachment = response.success)
                println("ViewModel addMedia - ${response.success}")
                }
                is NetworkState.Error -> throw RuntimeException("Error ${response.throwable}")
                else -> {}
            }
        }
    }
    private fun deleteEditPost() {
        newPost.value = editedPost
    }
}