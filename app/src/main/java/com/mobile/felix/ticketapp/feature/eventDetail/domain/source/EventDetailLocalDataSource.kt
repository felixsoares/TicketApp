package com.mobile.felix.ticketapp.feature.eventDetail.domain.source

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.domain.model.Order

interface EventDetailLocalDataSource {
    suspend fun getEventById(id: Long): Event
    suspend fun getOrderByEventId(eventId: Long): Order?
    suspend fun saveInitialOrder(event: Event, quantity: Int) : Long
}