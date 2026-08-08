package com.mobile.felix.ticketapp.feature.home.presentation.state

import com.mobile.felix.ticketapp.core.domain.model.Event

data class HomeUiState(
    val isLoading: Boolean = false,
    val events: List<Event>? = null,
)