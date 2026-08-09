package com.mobile.felix.ticketapp.feature.ticketDetail.presentation.state

import com.mobile.felix.ticketapp.core.domain.model.Order

data class TicketDetailUiState(
    val isLoading: Boolean = false,
    val order: Order? = null,
    val errorMessage: String? = null
)

