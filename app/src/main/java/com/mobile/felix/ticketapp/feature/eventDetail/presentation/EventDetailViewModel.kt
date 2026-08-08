package com.mobile.felix.ticketapp.feature.eventDetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.feature.eventDetail.data.usecase.GetEventByIdUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.action.EventDetailAction
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.state.EventDetailUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val getEventByIdUseCase: GetEventByIdUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<EventDetailAction>()

    var uiState = MutableStateFlow(EventDetailUiState())
        private set

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is EventDetailAction.Idle -> {}
                    is EventDetailAction.GetEventById -> getEvent(action.eventId)
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

    fun onAction(action: EventDetailAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}