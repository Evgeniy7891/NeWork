package ru.stan.nework.domain.models.network

data class Push(
    val recipientId: Long?,
    val content: String,
)
