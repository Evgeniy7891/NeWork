package ru.stan.nework.domain.usecase.events

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.repository.EventsRepository
import javax.inject.Inject

class GetEventByIdUseCase @Inject constructor(private val repository: EventsRepository) {
    suspend operator fun invoke(id: Long): NetworkState<EventModel> {
        return repository.getEventById(id)
    }
}