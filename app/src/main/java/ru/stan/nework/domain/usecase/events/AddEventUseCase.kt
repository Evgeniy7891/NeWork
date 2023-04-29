package ru.stan.nework.domain.usecase.events

import ru.stan.nework.domain.models.network.NetworkState
import ru.stan.nework.domain.models.network.event.EventModel
import ru.stan.nework.domain.models.ui.event.Event
import ru.stan.nework.domain.repository.EventsRepository
import javax.inject.Inject

class AddEventUseCase @Inject constructor(private val repository: EventsRepository) {

    suspend operator fun invoke(event: Event): NetworkState<EventModel> {
        return repository.addEvent(event)
    }
}