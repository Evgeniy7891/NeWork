package ru.stan.nework.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.stan.nework.domain.models.network.NetworkState
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): NetworkState<T> {
    return withContext(dispatcher) {
        try {
            NetworkState.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val code = throwable.code()
                    NetworkState.Error("Code $code : ${throwable.message ?: "Http error"}")
                }
                is IOException -> NetworkState.Error("Network error")
                else -> {
                    NetworkState.Error("Unknown error - ${throwable.message}")
                }
            }
        }
    }
}