package ru.stan.nework.domain.models.network.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Attachment(
    val type: String,
    val url: String
) : Parcelable