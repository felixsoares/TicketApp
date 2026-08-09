package com.mobile.felix.ticketapp.feature.tickets.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.tickets.domain.repository.TicketsRepository

class GetOrdersUseCase(private val repository: TicketsRepository) {
    suspend operator fun invoke(): List<Order> = repository.getOrders()
}

