package ru.stan.nework.data.room.entity

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import ru.stan.nework.data.room.Converters
import ru.stan.nework.domain.models.network.post.Attachment
import ru.stan.nework.domain.models.network.post.Coords
import ru.stan.nework.domain.models.network.post.PostModel
import ru.stan.nework.domain.models.ui.post.AttachmentType
import ru.stan.nework.domain.models.ui.post.Post

@Entity
@TypeConverters(Converters::class)
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
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
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    @Embedded
    val attachment: AttachmentEmbedded?,
    val ownedByMe: Boolean,
    val users: Map<Int, UserPreview>,
) {
    fun toDto() = Post(
        id, authorId, author, authorAvatar, authorJob, content,
        published, link, likeOwnerIds, mentionIds, mentionedMe,
        likedByMe, attachment?.toDto(), ownedByMe, users
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.authorJob,
                dto.content,
                dto.published,
                dto.link,
                dto.likeOwnerIds,
                dto.mentionIds,
                dto.mentionedMe,
                dto.likedByMe,
                AttachmentEmbedded.fromDto(dto.attachment),
                dto.ownedByMe,
                dto.users
            )
    }
}
    data class AttachmentEmbedded(
        var typeAttach: AttachmentType,
        var url: String
    ) {
        fun toDto() = Attachment(typeAttach, url)

        companion object {
            fun fromDto(dto: Attachment?) = dto?.let {
                AttachmentEmbedded(it.type, it.url)
            }
        }
    }

    @Parcelize
    data class UserPreview(
        val id: Int = 0,
        val name: String,
        val avatar: String?,
    ) : Parcelable

    fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)

