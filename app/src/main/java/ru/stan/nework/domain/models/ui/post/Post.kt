package ru.stan.nework.domain.models.ui.post

import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.Users

data class Post(
    val attachment: Attachment,
    val author: String,
    val authorAvatar: String,
    val authorId: Int,
    val authorJob: String,
    val content: String,
    val id: Int,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val link: String,
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val ownedByMe: Boolean,
    val published: String,
    val users: List<Users>
)
