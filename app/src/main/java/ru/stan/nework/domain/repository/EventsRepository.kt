package ru.stan.nework.domain.repository

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.models.ui.event.Event

interface EventsRepository {
    suspend fun getEvents() : NetworkState<List<Event>>
    suspend fun addEvent(event: Event) :  NetworkState<EventModel>
    suspend fun removeEvents(id: Long) :  NetworkState<EventModel>
    suspend fun getEventById(id: Long) :  NetworkState<EventModel>
    suspend fun deleteEventLike(id: Long):  NetworkState<EventModel>
    suspend fun addEventLike(id: Long):  NetworkState<EventModel>
}