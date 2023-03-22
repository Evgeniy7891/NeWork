package ru.stan.nework.domain.models.network.post

import ru.stan.nework.domain.models.IConvertableTo
import ru.stan.nework.domain.models.ui.post.Post

data class PostModel(
    val attachment: Attachment,
    val author: String,
    val authorAvatar: String,
    val authorId: Int,
    val authorJob: String,
    val content: String,
    val coords: Coords,
    val id: Int,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val link: String,
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val ownedByMe: Boolean,
    val published: String,
    val users: List<Users>
) : IConvertableTo<Post> {

   override fun convertTo() : Post {
        return Post(
            attachment = attachment,
            author = author,
            authorAvatar = authorAvatar,
            authorId = authorId,
            authorJob = authorJob,
            content = content,
            id = id,
            likeOwnerIds = likeOwnerIds,
            likedByMe = likedByMe,
            link = link,
            mentionIds = mentionIds,
            mentionedMe = mentionedMe,
            ownedByMe = ownedByMe,
            published = published,
            users = users
        )
    }
}
