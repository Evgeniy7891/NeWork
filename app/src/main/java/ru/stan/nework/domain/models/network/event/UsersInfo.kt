package ru.stan.nework.domain.models.network.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsersInfo(
    val avatar: String,
    val name: String
): Parcelable