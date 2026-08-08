package com.mobile.felix.ticketapp.feature.ticket.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.ticket.domain.repository.TicketRepository

class GetEventByIdUseCase(
    private val repository: TicketRepository
) {
    suspend fun invoke(eventId: Long): Event = repository.getEventById(eventId)
}