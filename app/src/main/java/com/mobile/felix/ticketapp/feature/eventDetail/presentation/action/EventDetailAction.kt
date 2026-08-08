package com.mobile.felix.ticketapp.feature.eventDetail.presentation.action

sealed interface EventDetailAction {
    data object Idle : EventDetailAction
    data class GetEventById(val eventId: Long) : EventDetailAction
}