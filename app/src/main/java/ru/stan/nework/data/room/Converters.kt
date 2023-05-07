package ru.stan.nework.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.stan.nework.data.room.entity.UserPreview
import ru.stan.nework.domain.models.ui.post.AttachmentType

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name

    @TypeConverter
    fun fromListInt(list: List<Int?>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toListInt(string: String?): List<Int> {
        val type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    fun fromUsers(map: Map<Int, UserPreview>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun toUsers(string: String): Map<Int, UserPreview> {
        val maptype = object : TypeToken<Map<Int, UserPreview>>() {}.type
        return Gson().fromJson(string, maptype)
    }
}