package com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.repository.TicketDetailRepository

class GetOrderByIdUseCase(private val repository: TicketDetailRepository) {
    suspend operator fun invoke(orderId: Long): Order? = repository.getOrderById(orderId)
}

