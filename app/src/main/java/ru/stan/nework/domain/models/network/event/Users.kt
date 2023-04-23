package ru.stan.nework.domain.models.network.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val users: List<UsersInfo> = emptyList()
): Parcelable