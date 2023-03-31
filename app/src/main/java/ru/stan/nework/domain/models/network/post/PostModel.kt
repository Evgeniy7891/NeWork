package ru.stan.nework.domain.models.network.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.stan.nework.domain.models.IConvertableTo
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post
@Parcelize
data class PostModel(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String?,
    val published: String?,
    val coords: Coords?,
    val link: String?,
    val likeOwnerIds: List<Int>,
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment?,
    val ownedByMe: Boolean,
) : IConvertableTo<Post>, Parcelable {

   override fun convertTo() : Post {
        return Post(
            attachment = attachment ?: Attachment(AttachmentType.IMAGE,""),
            author = author,
            authorAvatar = authorAvatar ?: "",
            authorId = authorId,
            authorJob = authorJob ?: "",
            content = content ?: "",
            id = id,
            likeOwnerIds = likeOwnerIds,
            likedByMe = likedByMe,
            link = link ?: "",
            mentionIds = mentionIds,
            mentionedMe = mentionedMe,
            ownedByMe = ownedByMe,
            published = published ?: "",
        )
    }
}
