package ru.stan.nework.domain.models.network.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.stan.nework.domain.models.network.post.Attachment

@Parcelize
data class EventRequest(
    var id: Int = 0,
    var content: String = "",
    val datetime: String? = null,
    val type: Type? = Type.OFFLINE,
    var attachment: Attachment? = null,
    var link: String? = null,
    val speakerIds: List<Int>? = null,
) : Parcelable