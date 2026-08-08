package com.mobile.felix.ticketapp.feature.eventDetail.domain.source

import com.mobile.felix.ticketapp.core.domain.model.Event

interface EventDetailLocalDataSource {
    suspend fun getEventById(id: Long): Event
}