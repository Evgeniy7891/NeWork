package ru.stan.nework.domain.models.network.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Users(
    val id : UserInfo,
) : Parcelable