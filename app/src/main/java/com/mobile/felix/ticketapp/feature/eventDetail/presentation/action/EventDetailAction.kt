package com.mobile.felix.ticketapp.feature.eventDetail.presentation.action

import com.mobile.felix.ticketapp.core.domain.model.Event

sealed interface EventDetailAction {
    data object Idle : EventDetailAction
    data class GetEventById(val eventId: Long) : EventDetailAction

    data class BuyTicket(val event: Event, val quantity: Int) : EventDetailAction
}