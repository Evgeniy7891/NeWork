package ru.stan.nework.domain.models.network.post

data class PostRequest(
    var id: Int,
    var content: String,
    var link: String?,
    var attachment: Attachment?,
    var mentionIds: List<Int>,
)
