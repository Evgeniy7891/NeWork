package ru.stan.nework.providers.datasources

import ru.stan.nework.data.datasources.EventsRemoteDataSource
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.models.network.event.EventRequest
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.providers.network.NetworkService
import javax.inject.Inject

class EventsRemoteDataSourceImpl @Inject constructor(private val apiService: NetworkService) :
    EventsRemoteDataSource {
    override suspend fun getEvents(): List<EventModel> {
        return apiService.getEvents()
    }

    override suspend fun addEvent(event: EventRequest): EventModel {
        return apiService.addEvent(event)
    }

    override suspend fun removeEvents(id: Long): EventModel {
        return apiService.removeEvent(id)
    }

    override suspend fun getEventById(id: Long): EventModel {
        return apiService.getEventById(id)
    }

    override suspend fun deleteEventLike(id: Long): EventModel {
        return apiService.deleteEventLike(id)
    }

    override suspend fun addEventLike(id: Long): EventModel {
       return apiService.likeEventById(id)
    }
}