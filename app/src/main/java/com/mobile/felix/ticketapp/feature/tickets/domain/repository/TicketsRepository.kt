package com.mobile.felix.ticketapp.feature.tickets.domain.repository

import com.mobile.felix.ticketapp.core.domain.model.Order

interface TicketsRepository {
    suspend fun getOrders(): List<Order>
}

