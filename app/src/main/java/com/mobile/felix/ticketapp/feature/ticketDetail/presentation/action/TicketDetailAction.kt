package com.mobile.felix.ticketapp.feature.ticketDetail.presentation.action

import com.mobile.felix.ticketapp.core.domain.model.Order

sealed interface TicketDetailAction {
    data object Idle : TicketDetailAction
    data class LoadOrder(val orderId: Long) : TicketDetailAction
    data class PaymentRequest(val order: Order) : TicketDetailAction
    data class PaymentActionUpdate(val json: String) : TicketDetailAction
}

