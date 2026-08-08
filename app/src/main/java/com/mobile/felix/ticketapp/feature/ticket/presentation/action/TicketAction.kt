package com.mobile.felix.ticketapp.feature.ticket.presentation.action

sealed interface TicketAction {
    data object Idle : TicketAction
    data class GetEventById(val eventId: Long) : TicketAction
}