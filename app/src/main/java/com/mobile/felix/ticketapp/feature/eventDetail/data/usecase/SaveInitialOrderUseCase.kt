package com.mobile.felix.ticketapp.feature.eventDetail.data.usecase

import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.feature.eventDetail.domain.repository.EventDetailRepository

class SaveInitialOrderUseCase(
    private val repository: EventDetailRepository
) {
    suspend fun invoke(event: Event, quantity: Int): Long =
        repository.saveInitialOrder(event, quantity)
}