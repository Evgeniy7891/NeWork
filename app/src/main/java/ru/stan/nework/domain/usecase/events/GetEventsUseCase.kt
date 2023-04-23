package ru.stan.nework.domain.usecase.events

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.repository.EventsRepository
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(private val repository: EventsRepository) {
    suspend operator fun invoke(): NetworkState<List<Event>> {
        return repository.getEvents()
    }
}