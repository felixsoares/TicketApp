package com.mobile.felix.ticketapp.feature.eventDetail.data.repository

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.eventDetail.domain.repository.EventDetailRepository
import com.mobile.felix.ticketapp.feature.eventDetail.domain.source.EventDetailLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventDetailRepositoryImpl(
    private val localDataSource: EventDetailLocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : EventDetailRepository {

    override suspend fun getEventById(eventId: Long): Event = withContext(dispatcher) {
        return@withContext localDataSource.getEventById(eventId)
    }

    override suspend fun getOrderByEventId(eventId: Long): Order? = withContext(dispatcher) {
        return@withContext localDataSource.getOrderByEventId(eventId)
    }

    override suspend fun saveInitialOrder(
        event: Event,
        quantity: Int
    ) : Long = withContext(dispatcher) {
        return@withContext localDataSource.saveInitialOrder(event, quantity)
    }
}