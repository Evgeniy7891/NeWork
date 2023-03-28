package ru.stan.nework.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.stan.nework.providers.network.Api
import ru.stan.nework.providers.network.AppAuth
import ru.stan.nework.providers.network.NetworkService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideAuthPrefs(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideApi(
        authPrefs: SharedPreferences
    ): NetworkService {
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .addInterceptor { chain ->
                        authPrefs.getString(AppAuth.tokenKey, null)?.let {token ->
                            val newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", token)
                                .build()
                            return@addInterceptor chain.proceed(newRequest)
                        }
                        chain.proceed(chain.request())
                    }
                    .connectTimeout(40, TimeUnit.SECONDS)
                    .writeTimeout(40, TimeUnit.SECONDS)
                    .readTimeout(40, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(NetworkService::class.java)
    }
}