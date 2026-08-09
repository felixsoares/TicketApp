package com.mobile.felix.ticketapp.feature.eventDetail.domain.repository

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.domain.model.Order

interface EventDetailRepository {
    suspend fun getEventById(eventId: Long): Event
    suspend fun getOrderByEventId(eventId: Long): Order?
    suspend fun saveInitialOrder(event: Event, quantity: Int): Long
}