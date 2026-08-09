package com.mobile.felix.ticketapp.feature.ticketDetail.domain.source

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus

interface TicketDetailLocalDataSource {
    suspend fun getOrderById(orderId: Long): Order?
    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus)
}

