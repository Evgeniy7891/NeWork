package ru.stan.nework.domain.models.ui.user

data class UserUI(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String,
    var isChecked: Boolean = false,
)
