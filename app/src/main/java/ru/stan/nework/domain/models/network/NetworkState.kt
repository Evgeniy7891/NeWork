package ru.stan.nework.domain.models.network

sealed class NetworkState<out Data> {

    class Loading<Data> : NetworkState<Data>()
    data class Success<Data>(val success: Data) : NetworkState<Data>()
    data class Error(val throwable: String) : NetworkState<Nothing>()
}
