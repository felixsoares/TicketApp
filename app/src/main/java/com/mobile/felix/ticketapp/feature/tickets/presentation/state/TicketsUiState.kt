package com.mobile.felix.ticketapp.feature.tickets.presentation.state

import com.mobile.felix.ticketapp.core.domain.model.Order

data class TicketsUiState(
    val isLoading: Boolean = false,
    val orders: List<Order>? = null,
    val errorMessage: String? = null
)

