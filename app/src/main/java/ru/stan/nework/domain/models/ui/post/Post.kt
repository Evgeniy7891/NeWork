package ru.stan.nework.domain.models.ui.post

import ru.stan.nework.data.room.entity.UserPreview
import ru.stan.nework.domain.models.network.post.Attachment

data class Post(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val link: String?,
    val likeOwnerIds: List<Int>,
    val mentionIds: List<Int>,
    val likedByMe: Boolean,
    val mentionedMe: Boolean,
    val attachment: Attachment?,
    val ownedByMe: Boolean,
    val users: Map<Int, UserPreview>,
)
