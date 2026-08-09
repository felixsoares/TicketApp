package com.mobile.felix.ticketapp.feature.ticketDetail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.feature.ticketDetail.data.usecase.GetOrderByIdUseCase
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.action.TicketDetailAction
import com.mobile.felix.ticketapp.feature.ticketDetail.presentation.state.TicketDetailUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TicketDetailViewModel(
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<TicketDetailAction>()

    var uiState = MutableStateFlow(TicketDetailUiState())
        private set

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is TicketDetailAction.Idle -> {}
                    is TicketDetailAction.LoadOrder -> loadOrder(action.orderId)
                }
            }
        }
    }

    private fun loadOrder(orderId: Long) = viewModelScope.launch {
        uiState.value = uiState.value.copy(isLoading = true)
        val result = runCatching { getOrderByIdUseCase(orderId) }
        uiState.value = uiState.value.copy(
            order = result.getOrNull(),
            errorMessage = result.exceptionOrNull()?.message,
            isLoading = false
        )
    }

    fun onAction(action: TicketDetailAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}

