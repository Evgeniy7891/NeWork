package ru.stan.nework.domain.models.network.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostRequest(
    var id: Int,
    var content: String,
    var link: String?,
    var attachment: Attachment?,
    var mentionIds: List<Int>,
) : Parcelable
