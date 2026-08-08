package com.mobile.felix.ticketapp.feature.ticket.domain.source

import com.mobile.felix.ticketapp.core.domain.model.Event

interface TicketLocalDataSource {
    suspend fun getEventById(id: Long): Event
}