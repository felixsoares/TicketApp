package com.mobile.felix.ticketapp.feature.eventDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.feature.eventDetail.domain.repository.EventDetailRepository

class GetOrderByEventIdUseCase(
    private val repository: EventDetailRepository
) {
    suspend fun invoke(eventId: Long): Order? = repository.getOrderByEventId(eventId)
}