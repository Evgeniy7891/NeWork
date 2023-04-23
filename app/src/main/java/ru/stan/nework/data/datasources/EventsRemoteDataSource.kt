package ru.stan.nework.data.datasources

import ru.stan.nework.domain.models.network.event.EventModel

interface EventsRemoteDataSource {
    suspend fun getEvents() : List<EventModel>
}