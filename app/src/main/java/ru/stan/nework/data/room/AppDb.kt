package ru.stan.nework.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.stan.nework.data.room.dao.EventDao
import ru.stan.nework.data.room.dao.EventRemoteKeyDao
import ru.stan.nework.data.room.dao.PostDao
import ru.stan.nework.data.room.dao.PostRemoteKeyDao
import ru.stan.nework.data.room.entity.EventEntity
import ru.stan.nework.data.room.entity.PostEntity
import ru.stan.nework.data.room.entity.PostRemoteKeyEntity
import ru.stan.nework.data.room.entity.EventRemoteKeyEntity

@Database(
    entities = [
        PostEntity::class,
        EventEntity::class,
        EventRemoteKeyEntity::class,
        PostRemoteKeyEntity::class
    ], version = 2, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun eventDao(): EventDao
    abstract fun eventRemoteKeyDao(): EventRemoteKeyDao
}