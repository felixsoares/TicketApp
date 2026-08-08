package com.mobile.felix.ticketapp.feature.eventDetail.presentation.state

import com.mobile.felix.ticketapp.core.domain.model.Event

data class EventDetailUiState(
    val isLoading: Boolean = false,
    val event: Event? = null
)