package com.mobile.felix.ticketapp.feature.ticketDetail.domain.source

import com.mobile.felix.ticketapp.core.domain.model.Order

interface TicketDetailLocalDataSource {
    suspend fun getOrderById(orderId: Long): Order
}

