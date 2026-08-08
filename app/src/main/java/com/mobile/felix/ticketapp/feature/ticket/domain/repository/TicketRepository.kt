package com.mobile.felix.ticketapp.feature.ticket.domain.repository

import com.mobile.felix.ticketapp.core.domain.Event

interface TicketRepository {
    suspend fun getEventById(eventId: Long): Event
}