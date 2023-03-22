package ru.stan.nework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.stan.nework.data.datasources.PostRemoteDataSource
import ru.stan.nework.data.repository.PostRepositoryImpl
import ru.stan.nework.domain.repository.PostRepository
import ru.stan.nework.providers.datasources.PostRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    abstract fun bindPostRemoteDataSource(impl: PostRemoteDataSourceImpl): PostRemoteDataSource
}