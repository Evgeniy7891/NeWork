package ru.stan.nework.data.repository.events

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.stan.nework.data.datasources.EventsRemoteDataSource
import ru.stan.nework.data.repository.EventRemoteMediator
import ru.stan.nework.data.room.dao.EventDao
import ru.stan.nework.data.room.entity.EventEntity
import ru.stan.nework.data.room.entity.UserPreview
import ru.stan.nework.data.room.entity.toEntity
import ru.stan.nework.di.IoDispatcher
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.models.network.event.EventRequest
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.repository.EventsRepository
import ru.stan.nework.utils.safeApiCall
import javax.inject.Inject
@OptIn(ExperimentalPagingApi::class)
class EventRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val remoteDataSource: EventsRemoteDataSource,
    mediator: EventRemoteMediator,
    private val eventDao: EventDao,
) : EventsRepository {


    override val data: Flow<PagingData<Event>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { eventDao.getAllEvents() },
            remoteMediator = mediator
        ).flow.map {
            it.map(EventEntity::toDto)
        }

    override val eventUsersData: MutableLiveData<List<UserPreview>> =
        MutableLiveData(emptyList())

    override suspend fun getEvents(): NetworkState<List<Event>> {
        return safeApiCall(ioDispatcher) {
            remoteDataSource.getEvents().map { it.convertTo() }
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