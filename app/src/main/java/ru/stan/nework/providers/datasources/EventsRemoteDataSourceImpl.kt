package ru.stan.nework.providers.datasources

import ru.stan.nework.data.datasources.EventsRemoteDataSource
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.providers.network.NetworkService
import javax.inject.Inject

class EventsRemoteDataSourceImpl @Inject constructor(private val apiService: NetworkService): EventsRemoteDataSource {
    override suspend fun getEvents(): List<EventModel> {
        return apiService.getEvents()
    }
}