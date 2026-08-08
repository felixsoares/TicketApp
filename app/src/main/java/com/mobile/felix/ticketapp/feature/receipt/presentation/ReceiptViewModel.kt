package com.mobile.felix.ticketapp.feature.receipt.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.felix.ticketapp.feature.receipt.data.usecase.GetOrderByIdUseCase
import com.mobile.felix.ticketapp.feature.receipt.presentation.action.ReceiptAction
import com.mobile.felix.ticketapp.feature.receipt.presentation.state.ReceiptUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ReceiptViewModel(
    private val getOrderByIdUseCase: GetOrderByIdUseCase
) : ViewModel() {

    private val pendingActions = MutableSharedFlow<ReceiptAction>()

    var uiState = MutableStateFlow(ReceiptUiState())
        private set

    init {
        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    is ReceiptAction.Idle -> {}
                    is ReceiptAction.LoadOrder -> loadOrder(action.orderId)
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

    fun onAction(action: ReceiptAction) = viewModelScope.launch {
        pendingActions.emit(action)
    }
}

