package ru.stan.nework.domain.models.network.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.stan.nework.data.room.entity.UserPreview
import ru.stan.nework.domain.models.IConvertableTo
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.Coords
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.models.ui.event.EventType
import ru.stan.nework.domain.models.ui.post.AttachmentType

@Parcelize
data class EventModel(
    val attachment: Attachment? = null,
    val author: String,
    val authorAvatar: String?,
    val authorId: Int,
    val authorJob: String?,
    val content: String?,
    val coords: Coords? = null,
    val datetime: String?,
    val id: Int,
    val likeOwnerIds: List<Int> = emptyList(),
    val likedByMe: Boolean = false,
    val link: String? = null,
    val ownedByMe: Boolean,
    val participantsIds: List<Int> = emptyList(),
    val participatedByMe: Boolean = false,
    val published: String?,
    val speakerIds: List<Int> = emptyList(),
    val type: String?,
    val users: Map<Int, UserPreview>,
) : IConvertableTo<Event>, Parcelable {

    override fun convertTo(): Event {
        return Event(
            attachment = attachment ?: Attachment(AttachmentType.IMAGE, ""),
            author = author,
            authorAvatar = authorAvatar ?: "",
            authorId = authorId,
            authorJob = authorJob ?: "",
            content = content ?: "",
            id = id,
            likeOwnerIds = likeOwnerIds,
            likedByMe = likedByMe,
            link = link ?: "",
            ownedByMe = ownedByMe,
            published = published ?: "",
            datetime = datetime ?: "",
            participantsIds = participantsIds,
            speakerIds = speakerIds,
            users = users,
            participatedByMe = false,
            type = EventType.OFFLINE
        )
    }
}