package com.mobile.felix.ticketapp.feature.home.domain.repository

import com.mobile.felix.ticketapp.core.domain.model.Event

interface HomeRepository {
    suspend fun getEvents() : List<Event>
}