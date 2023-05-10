package ru.stan.nework.domain.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.stan.nework.data.room.entity.UserPreview
import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.models.network.event.EventRequest
import ru.stan.nework.domain.models.ui.event.Event

interface EventsRepository {

    val data: Flow<PagingData<Event>>

    val eventUsersData: MutableLiveData<List<UserPreview>>
    suspend fun getEvents() : NetworkState<List<Event>>
    suspend fun addEvent(event: EventRequest) :  NetworkState<EventModel>
    suspend fun removeEvents(id: Long) :  NetworkState<EventModel>
    suspend fun getEventById(id: Long) :  NetworkState<EventModel>
    suspend fun deleteEventLike(id: Long):  NetworkState<EventModel>
    suspend fun addEventLike(id: Long):  NetworkState<EventModel>
}