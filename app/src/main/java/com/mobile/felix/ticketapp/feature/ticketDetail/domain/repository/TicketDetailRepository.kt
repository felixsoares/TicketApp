package com.mobile.felix.ticketapp.feature.ticketDetail.domain.repository

import com.mobile.felix.ticketapp.core.domain.model.Order

interface TicketDetailRepository {
    suspend fun getOrderById(orderId: Long): Order
}

