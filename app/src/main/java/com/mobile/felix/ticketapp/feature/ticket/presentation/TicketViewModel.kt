package com.mobile.felix.ticketapp.feature.ticket.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.feature.home.presentation.action.HomeAction
import com.mobile.felix.ticketapp.feature.ticket.data.usecase.GetEventByIdUseCase
import com.mobile.felix.ticketapp.feature.ticket.presentation.action.TicketAction
import com.mobile.felix.ticketapp.feature.ticket.presentation.state.TicketUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TicketViewModel(
    private val getEventByIdUseCase: GetEventByIdUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<TicketAction>()

    var uiState = MutableStateFlow(TicketUiState())
        private set

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is TicketAction.Idle -> {}
                    is TicketAction.GetEventById -> getEvent(action.eventId)
                }
            }
        }
    }

    private fun getEvent(eventId: Long) = viewModelScope.launch {
        uiState.value = uiState.value.copy(isLoading = true)
        val event = getEventByIdUseCase.invoke(eventId)
        uiState.value = uiState.value.copy(
            event = event,
            isLoading = false,
        )
    }

    fun onAction(action: TicketAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}