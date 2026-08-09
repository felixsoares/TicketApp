package com.mobile.felix.ticketapp.feature.tickets.presentation.action

sealed interface TicketsAction {
    data object Idle : TicketsAction
    data object LoadOrders : TicketsAction
}

