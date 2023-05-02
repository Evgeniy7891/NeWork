package ru.stan.nework.domain.models.network.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.stan.nework.domain.models.IConvertableTo
import ru.stan.nework.domain.models.ui.user.UserUI

@Parcelize
data class User(
    val id: Int,
    val login: String,
    val name: String,
    val avatar: String?,
    var isChecked: Boolean = false,
) : Parcelable, IConvertableTo<UserUI> {
    override fun convertTo(): UserUI {
        return UserUI(
            id = id,
            login = login,
            name = name,
            avatar = avatar ?: "",
            isChecked = isChecked
        )
    }
}
