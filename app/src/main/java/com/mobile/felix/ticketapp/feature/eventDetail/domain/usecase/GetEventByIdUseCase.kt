package com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.eventDetail.domain.repository.EventDetailRepository

class GetEventByIdUseCase(
    private val repository: EventDetailRepository
) {
    suspend fun invoke(eventId: Long): Event = repository.getEventById(eventId)
}