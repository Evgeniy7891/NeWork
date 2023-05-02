package ru.stan.nework.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import ru.stan.nework.data.datasources.EventsRemoteDataSource
import ru.stan.nework.di.IoDispatcher
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.models.network.event.EventRequest
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.repository.EventsRepository
import ru.stan.nework.utils.safeApiCall
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteDataSource: EventsRemoteDataSource
) : EventsRepository {

    override suspend fun getEvents(): NetworkState<List<Event>> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getEvents()
                .map { it.convertTo() }
        }
    }
    override suspend fun addEvent(event: EventRequest): NetworkState<EventModel> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.addEvent(event)
        }
    }

    override suspend fun removeEvents(id: Long): NetworkState<EventModel> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.removeEvents(id)
        }
    }

    override suspend fun getEventById(id: Long): NetworkState<EventModel> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getEventById(id)
        }
    }

    override suspend fun deleteEventLike(id: Long): NetworkState<EventModel> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.removeEvents(id)
        }
    }

    override suspend fun addEventLike(id: Long): NetworkState<EventModel> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.addEventLike(id)
        }
    }
}