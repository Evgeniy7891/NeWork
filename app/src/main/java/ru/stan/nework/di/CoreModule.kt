package ru.stan.nework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.stan.nework.data.datasources.AuthRemoteDataSource
import ru.stan.nework.data.datasources.PostRemoteDataSource
import ru.stan.nework.data.repository.AuthRepositoryImpl
import ru.stan.nework.data.repository.PostRepositoryImpl
import ru.stan.nework.domain.repository.AuthRepository
import ru.stan.nework.domain.repository.PostRepository
import ru.stan.nework.providers.datasources.AuthRemoteDataSourceImpl
import ru.stan.nework.providers.datasources.PostRemoteDataSourceImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class CoreModule {

    @Binds
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    abstract fun bindPostRemoteDataSource(impl: PostRemoteDataSourceImpl): PostRemoteDataSource

    @Binds
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl) : AuthRepository

    @Binds
    abstract fun bindAuthRemoteDataSource(impl: AuthRemoteDataSourceImpl): AuthRemoteDataSource


}