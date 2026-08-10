package com.mobile.felix.ticketapp.feature.home.domain.usecase

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.home.domain.repository.HomeRepository

class GetEventsUseCase(
    private val repository: HomeRepository
) {
    suspend fun invoke(): List<Event> {
        return repository.getEvents()
    }
}