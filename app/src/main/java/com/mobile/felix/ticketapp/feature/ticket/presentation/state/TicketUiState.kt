package com.mobile.felix.ticketapp.feature.ticket.presentation.state

import com.mobile.felix.ticketapp.core.domain.model.Event

data class TicketUiState(
    val isLoading: Boolean = false,
    val event: Event? = null
)