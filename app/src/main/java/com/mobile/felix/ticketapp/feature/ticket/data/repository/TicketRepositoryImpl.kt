package com.mobile.felix.ticketapp.feature.ticket.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.ticket.domain.repository.TicketRepository
import com.mobile.felix.ticketapp.feature.ticket.domain.source.TicketLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TicketRepositoryImpl(
    private val localDataSource: TicketLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TicketRepository {

    override suspend fun getEventById(eventId: Long): Event = withContext(dispatcher) {
        return@withContext localDataSource.getEventById(eventId)
    }
}