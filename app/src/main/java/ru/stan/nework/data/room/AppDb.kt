package ru.stan.nework.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.stan.nework.data.room.dao.PostDao
import ru.stan.nework.data.room.dao.PostRemoteKeyDao
import ru.stan.nework.data.room.entity.PostEntity
import ru.stan.nework.data.room.entity.PostRemoteKeyEntity

@Database(
    entities = [
        PostEntity::class,
        PostRemoteKeyEntity::class,
    ], version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
}