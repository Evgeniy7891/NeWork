package ru.stan.nework.domain.models.ui.event

import ru.stan.nework.data.room.entity.UserPreview
import ru.stan.nework.domain.models.network.event.Users
import ru.stan.nework.domain.models.network.post.Attachment

data class Event(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val type: EventType,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val speakerIds: List<Int>,
    val participantsIds: List<Int>,
    val participatedByMe: Boolean,
    val attachment: Attachment?,
    val link: String?,
    val ownedByMe: Boolean,
    val users: Map<Int, UserPreview>,
)
enum class EventType{
    OFFLINE, ONLINE
}
