package ru.stan.nework.domain.models.network.post

data class PostRequest(
    val id: Int,
    val content: String,
    val link: String?,
    val attachment: Attachment?,
    val mentionIds: List<Int>,
)
