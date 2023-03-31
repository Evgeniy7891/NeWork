package ru.stan.nework.domain.usecase.post

import okhttp3.MultipartBody
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.repository.PostRepository
import javax.inject.Inject

class AddMultiMediaUseCase @Inject constructor(private val repository: PostRepository){

    suspend operator fun invoke(type: AttachmentType, file: MultipartBody.Part): NetworkState<Attachment> {
        return repository.addMultimedia(type, file)
    }
}