package ru.stan.nework.domain.models.ui.event

import ru.stan.nework.domain.models.network.event.Users
import ru.stan.nework.domain.models.network.post.Attachment

data class Event(
    val attachment: Attachment,
    val author: String,
    val authorAvatar: String,
    val authorId: Int,
    val authorJob: String,
    val content: String,
    val datetime: String,
    val id: Int,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val link: String,
    val ownedByMe: Boolean,
    val participantsIds: List<Int>,
    val published: String,
    val speakerIds: List<Int>,
    val users: Users
)
