package ru.stan.nework.domain.models.network.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.stan.nework.domain.models.ui.post.AttachmentType

@Parcelize
data class Attachment(
    var type: AttachmentType,
    var url: String
) : Parcelable