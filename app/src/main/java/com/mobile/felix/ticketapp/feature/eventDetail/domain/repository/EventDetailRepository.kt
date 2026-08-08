package com.mobile.felix.ticketapp.feature.eventDetail.domain.repository

import com.mobile.felix.ticketapp.core.domain.model.Event

interface EventDetailRepository {
    suspend fun getEventById(eventId: Long): Event
}