package com.mobile.felix.ticketapp.feature.tickets.domain.source

import com.mobile.felix.ticketapp.core.domain.model.Order

interface TicketsLocalDataSource {
    suspend fun getOrders(): List<Order>
    suspend fun initOrders()
}

