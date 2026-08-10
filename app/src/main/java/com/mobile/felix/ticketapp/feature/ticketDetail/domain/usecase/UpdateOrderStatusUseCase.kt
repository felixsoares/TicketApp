package com.mobile.felix.ticketapp.feature.ticketDetail.domain.usecase

import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.ticketDetail.domain.repository.TicketDetailRepository

class UpdateOrderStatusUseCase(
    private val ticketDetailRepository: TicketDetailRepository
) {
    suspend fun invoke(orderId: Long, status: OrderStatus) = ticketDetailRepository.updateOrderStatus(orderId, status)
}