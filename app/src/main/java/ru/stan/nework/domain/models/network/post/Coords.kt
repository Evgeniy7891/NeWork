package ru.stan.nework.domain.models.network.post

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coords(
    val lat: String,
    val long: String
) : Parcelable