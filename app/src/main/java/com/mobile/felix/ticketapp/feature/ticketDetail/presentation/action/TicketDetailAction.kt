package com.mobile.felix.ticketapp.feature.ticketDetail.presentation.action

sealed interface TicketDetailAction {
    data object Idle : TicketDetailAction
    data class LoadOrder(val orderId: Long) : TicketDetailAction
}

