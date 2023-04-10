package ru.stan.nework.domain.models.network.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.stan.nework.domain.models.ui.post.AttachmentType

@Parcelize
data class Attachment(
    val type: AttachmentType?,
    val url: String?
) : Parcelable