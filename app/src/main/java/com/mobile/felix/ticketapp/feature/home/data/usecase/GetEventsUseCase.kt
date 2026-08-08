package com.mobile.felix.ticketapp.feature.home.data.usecase

import com.mobile.felix.ticketapp.core.domain.Event
import com.mobile.felix.ticketapp.feature.home.domain.repository.HomeRepository

class GetEventsUseCase(
    private val repository: HomeRepository
) {
    suspend fun invoke(): List<Event> {
        return repository.getEvents()
    }
}