package com.mobile.felix.ticketapp.feature.home.domain.source

import com.mobile.felix.ticketapp.core.domain.Event

interface HomeLocalDataSource {
    suspend fun getEvents(): List<Event>
    suspend fun initDatabase()
}