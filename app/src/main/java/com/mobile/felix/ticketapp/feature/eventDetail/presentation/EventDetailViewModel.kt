package com.mobile.felix.ticketapp.feature.eventDetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.core.domain.model.Event
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus
import com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase.GetEventByIdUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase.GetOrderByEventIdUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.domain.usecase.SaveInitialOrderUseCase
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.action.EventDetailAction
import com.mobile.felix.ticketapp.feature.eventDetail.presentation.state.EventDetailUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class EventDetailViewModel(
    private val getEventByIdUseCase: GetEventByIdUseCase,
    private val getOrderByEventIdUseCase: GetOrderByEventIdUseCase,
    private val saveInitialOrderUseCase: SaveInitialOrderUseCase
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
                    is EventDetailAction.BuyTicket -> buyTicket(action.event, action.quantity)
                }
            }
        }
    }

    private fun buyTicket(event: Event, quantity: Int) = viewModelScope.launch {
        uiState.value = uiState.value.copy(isLoading = true)
        val order = getOrderByEventIdUseCase.invoke(event.id)
        if (order == null || (order.status == OrderStatus.CANCELLED || order.status == OrderStatus.DENIED)) {
            val orderId = saveInitialOrderUseCase.invoke(event, quantity)
            uiState.value = uiState.value.copy(
                isLoading = false,
                orderId = orderId
            )
        } else {
            uiState.value =
                uiState.value.copy(
                    isLoading = false,
                    message = "Você já possui um pedido para este evento."
                )
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