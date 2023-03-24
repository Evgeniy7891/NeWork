package ru.stan.nework.domain.models.network.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
    val avatar: String,
    val name: String
) : Parcelable