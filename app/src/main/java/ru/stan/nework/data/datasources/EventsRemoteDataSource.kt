package ru.stan.nework.data.datasources

import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.models.ui.event.Event

interface EventsRemoteDataSource {
    suspend fun getEvents() : List<EventModel>
    suspend fun addEvent(event: Event) : EventModel
    suspend fun removeEvents(id: Long) : EventModel
    suspend fun getEventById(id: Long) : EventModel
    suspend fun deleteEventLike(id: Long): EventModel
    suspend fun addEventLike(id: Long): EventModel
}