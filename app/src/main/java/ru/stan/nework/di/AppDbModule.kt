package ru.stan.nework.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.stan.nework.data.room.AppDb
import ru.stan.nework.data.room.dao.EventDao
import ru.stan.nework.data.room.dao.EventRemoteKeyDao
import ru.stan.nework.data.room.dao.PostDao
import ru.stan.nework.data.room.dao.PostRemoteKeyDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module()
class AppDbModule {
    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDb =
        Room.databaseBuilder(
            context,
            AppDb::class.java,
            "app.db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Provides
    fun providePostDao(appDb: AppDb): PostDao = appDb.postDao()

    @Provides
    fun providePostRemoteKeyDao(appDb: AppDb): PostRemoteKeyDao =
        appDb.postRemoteKeyDao()

    @Provides
    fun provideEventDao(appDb: AppDb): EventDao = appDb.eventDao()

    @Provides
    fun provideEventRemoteKeyDao(appDb: AppDb): EventRemoteKeyDao =
        appDb.eventRemoteKeyDao()

}