package com.mobile.felix.ticketapp.feature.tickets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.feature.tickets.data.usecase.GetOrdersUseCase
import com.mobile.felix.ticketapp.feature.tickets.presentation.action.TicketsAction
import com.mobile.felix.ticketapp.feature.tickets.presentation.state.TicketsUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TicketsViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<TicketsAction>()

    var uiState = MutableStateFlow(TicketsUiState())
        private set

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is TicketsAction.Idle -> {}
                    is TicketsAction.LoadOrders -> loadOrders()
                }
            }
        }
    }

    private fun loadOrders() = viewModelScope.launch {
        uiState.value = uiState.value.copy(isLoading = true)
        val result = runCatching { getOrdersUseCase() }
        uiState.value = uiState.value.copy(
            orders = result.getOrNull(),
            errorMessage = result.exceptionOrNull()?.message,
            isLoading = false
        )
    }

    fun onAction(action: TicketsAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}

